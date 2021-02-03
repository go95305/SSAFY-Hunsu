package com.hindsight.authdemo.controller.v1;

import com.hindsight.authdemo.config.security.JwtTokenProvider;
import com.hindsight.authdemo.entity.User;
import com.hindsight.authdemo.model.response.CommonResult;
import com.hindsight.authdemo.model.response.SingleResult;
import com.hindsight.authdemo.model.social.KakaoProfile;
import com.hindsight.authdemo.model.social.SocialSignUp;
import com.hindsight.authdemo.repository.UserJpaRepo;
import com.hindsight.authdemo.service.ResponseService;
import com.hindsight.authdemo.service.user.KakaoService;
import com.hindsight.authdemo.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/auth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final KakaoService kakaoService;
    private final UserService userService;

    private final Environment env;
    @Value("${spring.url.base}")
    private String baseUrl;

    @Value("${spring.social.kakao.client_id}")
    private String kakaoClientId;

    @Value("${spring.social.kakao.redirect}")
    private String kakaoRedirect;


    @ApiOperation(value="kakao 로그인 테스트 URL (O)")
    @GetMapping(value="/url")
    public String socialLogin(){
        StringBuilder loginUrl = new StringBuilder()
                .append(env.getProperty("spring.social.kakao.url.login"))
                .append("?client_id=").append(kakaoClientId)
                .append("&response_type=code")
                .append("&redirect_uri=").append(baseUrl).append(kakaoRedirect);
        return loginUrl.toString();
    }

    @ResponseBody
    @ApiOperation(value="회원 체크 (O)", notes="유저 여부 판단, code:1 이면 로그인 (토큰받기), code:-1이면 회원가입 /signup 으로 리다이렉트: ")
    @GetMapping(value="/usercheck")
    public HashMap<String,String> usercheck(
            @ApiParam(value = "인가 코드", required = true) @RequestParam("code") String authorize_code){
        System.out.println("Auth_code: "+authorize_code);

        HashMap<String,String> kmap =new HashMap<>();
        Map<String,String> map =kakaoService.getAccessToken(authorize_code);
        System.out.println("Auth_code: "+authorize_code);
        String accessToken=map.get("accessToken");
        System.out.println("accessToken:" + accessToken);
        String refreshToken=map.get("refreshToken");
        System.out.println("refreshToken: " + refreshToken);

        // 카카오 서버에서 받은 엑세스 토큰으로 카카오 프로필 가져오기
        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        System.out.println("profile : " + profile);
        // 프로필의 id로 db에서 사람찾기
        Optional<User> user = Optional.ofNullable(userJpaRepo.findUserByUidAndFlag(profile.getUid(), false));
        System.out.println("User: "+user);
        if(user.isPresent()) { // 있으면
            System.out.println("로그인!!!");
            // 토큰 값들 수정 !!
            String jwtToken=jwtTokenProvider.generateToken(profile.getUid(),user.get().getRoles());
            String jwtRefresh=jwtTokenProvider.generateRefreshToken(profile.getUid(),user.get().getRoles());
            userService.setAllTokens(profile.getUid(),accessToken,refreshToken,jwtRefresh,jwtToken);
            System.out.println("토큰수정완료");
            kmap.put("code","1");
            kmap.put("jwtToken",jwtToken);
            kmap.put("jwtRefresh",jwtRefresh);
            return kmap;       //로그인
        }else{
            System.out.println("회원가입!!!");
            userService.joinUser(profile.getUid(),accessToken,refreshToken,false);
            System.out.println("가입완료");
            kmap.put("code","-1");
            return kmap;        // 회원가입
        }
//        return kmap;
    }

    @ApiOperation(value="소셜 로그인 (~)", notes="jwtAccess 토큰 검증, fail이면 실패 아니면 jwtAccessToken")
    @PostMapping(value="/login")
    public String login(
            @ApiParam(value = "jwtAccessToken", required = true) @RequestParam String jwtToken){

        if(jwtTokenProvider.validateToken(jwtToken)){    // jwt리프레시토큰이 유효할 때
            Optional<User> user = userJpaRepo.findUserByJwtToken(jwtToken);
            System.out.println("USER :" + user.get().getUid());
            String kakaoRefresh=user.get().getRefreshToken();
            // 카카오 리프레시 토큰 활용해서 카카오 엑세스 토큰 요청
            String kakaoAccess=kakaoService.reKakaoAccessToken(kakaoRefresh);
            if(!jwtTokenProvider.validateToken(kakaoRefresh)){  // 카카오 리프레시가 유효하지 않으면
                return "fail";                                  // 실패했다고 리턴
            }else{                                              // 유효하면
                //카카오 엑세스 토큰 저장
                userService.updateAccessToken(user.get().getUid(),kakaoAccess);
                //jwtRefreshToken을 통해 jwtAccessToken생성 후 프론트에 전달
                String jwtAccessToken = jwtTokenProvider.generateToken(user.get().getUid(),user.get().getRoles());
                System.out.println(jwtAccessToken);

                return  jwtAccessToken;
            }
        }else{
            return "fail";
        }

    }


    @ApiOperation(value="소셜 계정 가입 (O)", notes="추가 정보 form를 받아와 소셜 계정 회원가입을 한다.")
    @PostMapping(value ="/signup")
    public CommonResult signupProvider(@ApiParam("소셜 회원가입 객체") @RequestBody SocialSignUp form){
        System.out.println("Form uid: "+form.getUid());
        User user = userJpaRepo.findUserByUidAndFlag(form.getUid(),false);

        System.out.println("USER: "+user.getUid());
        String jwtAccess = jwtTokenProvider.generateToken(form.getUid(),user.getRoles());
        String jwtRefresh= jwtTokenProvider.generateRefreshToken(form.getUid(),user.getRoles());
        userService.addUserInfo(form.getUid(),form.getNickname(), form.getHeight(), form.getSize(),jwtRefresh,jwtAccess,true);
        System.out.println("성공!!");
        return responseService.getSuccessResult();
    }

    @ApiOperation(value="엑세스 토큰 검증 (O)",notes = "return 1 : 유효, return 0 : 유효하지않음")
    @GetMapping(value="/check")
    public int checkToken(@ApiParam("검증할 jwt엑세스토큰") @RequestParam String jwtToken){
        System.out.println("Check Start!!!!!!!!!!");
        if(jwtTokenProvider.validateToken(jwtToken)){
            System.out.println("유효");
            return 1;
        }else
            System.out.println("유효하지않음");
            return 0;   // 토큰 재발급으로
    }

    @ApiOperation(value="토큰 재발급 (O)", notes="리프레시 토큰으로 액세스 토큰을 재발급 한다.")
    @GetMapping("/newtoken")
    public SingleResult<String> getTokenFromRefreshToken(@ApiParam("jwtRefrshToken") @RequestParam String jwtRefresh){
        String accessToken = null;
        System.out.println("RefreshToken을 활용하여 AccessToken 갱신!!");
        if(jwtTokenProvider.validateToken(jwtRefresh)){
            long Uid =Long.parseLong(jwtTokenProvider.getUserPk(jwtRefresh));
            System.out.println("Uid: " + Uid);
            List<String> roles = jwtTokenProvider.getRoles(jwtRefresh);
            accessToken = jwtTokenProvider.generateToken(Uid, roles);

        }else{
            responseService.getFailResult(-1, "유효한토큰이없습니다.");
        }
        return responseService.getSingleResult(accessToken);
    }


}