package com.hindsight.authdemo.controller.v1;

import com.hindsight.authdemo.advice.exception.CUserExistException;
import com.hindsight.authdemo.advice.exception.CUserNotFoundException;
import com.hindsight.authdemo.config.security.JwtTokenProvider;
import com.hindsight.authdemo.entity.User;
import com.hindsight.authdemo.model.response.SingleResult;
import com.hindsight.authdemo.model.social.SocialSignUp;
import com.hindsight.authdemo.model.response.CommonResult;
import com.hindsight.authdemo.model.social.KakaoProfile;
import com.hindsight.authdemo.model.social.RetKakaoAuth;
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

import java.util.*;

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

    @ApiOperation(value="회원 체크 (~)", notes="유저 여부 판단")
    @PostMapping(value="/usercheck")
    public boolean usercheck(
            @ApiParam(value = "소셜 access_token", required = true) @RequestBody String socialAccessToken,String socialRefreshToken){

        // 카카오 서버에서 받은 엑세스 토큰으로 카카오 프로필 가져오기
        KakaoProfile profile = kakaoService.getKakaoProfile(socialAccessToken);
        // 프로필의 id로 db에서 사람찾기
        Optional<User> user = userJpaRepo.findUserByUidAndFlag(String.valueOf(profile.getUid()),true);

        if(user==null) { // 없으면
            userService.joinUser(profile.getUid(),socialAccessToken,socialRefreshToken,profile.getGender(),false);
            return false;       //회원가입
        }else{
            return true;        // 로그인
        }
    }

    @ApiOperation(value="소셜 로그인 (O)", notes="리프레시 토큰 검증, fail이면 실패 아니면 jwtAccessToken")
    @PostMapping(value="/login")
    public String login(
            @ApiParam(value = "jwtRefreshToken", required = true) @RequestBody String jwtRefreshToken){

        if(jwtTokenProvider.validateToken(jwtRefreshToken)){    // jwt리프레시토큰이 유효할 때
            Optional<User> user = userJpaRepo.findUserByJwtRefresh(jwtRefreshToken);
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
                return  jwtAccessToken;
            }
        }else{
            return "fail";
        }

    }


    @ApiOperation(value="소셜 계정 가입 (O)", notes="소셜 계정 회원가입을 한다.")
    @PostMapping(value ="/signup")
    public CommonResult signupProvider(@ApiParam("소셜 회원가입 객체") @RequestBody SocialSignUp form){

        Optional<User> user = userJpaRepo.findUserByUidAndFlag(form.getUID(),false);
        String jwtAccess = jwtTokenProvider.generateToken(form.getUID(),user.get().getRoles());
        String jwtRefresh= jwtTokenProvider.generateRefreshToken(form.getUID(),user.get().getRoles());
        userService.addUserInfo(form.getUID(),form.getNickname(), form.getHeight(), form.getSize(),jwtRefresh,jwtAccess,true);

        return responseService.getSuccessResult();
    }

    @ApiOperation(value="엑세스 토큰 검증 (X)")
    @GetMapping(value="/check")
    public int checkToken(@ApiParam("검증할 토큰") @RequestBody String accessToken){
        if(jwtTokenProvider.validateToken(accessToken)){
            return 1;
        }else
            return 0;   // 토큰 재발급으로
    }

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

//    @GetMapping(value="/login/kakao")
//    public RetKakaoAuth redirectKakao(@RequestParam(value="code", required = true) String code){
//        logger.info(code);
//        return kakaoService.getKakaoTokenInfo(code);
//    }

    @ApiOperation(value="토큰 재발급 (~)", notes="리프레시 토큰으로 액세스 토큰을 재발급 한다.")
    @GetMapping("/newtoken/{refreshToken}")
    public SingleResult<String> getTokenFromRefreshToken(@PathVariable String refreshToken){
        String accessToken = null;

        if(jwtTokenProvider.validateToken(refreshToken)){
            String Uid = jwtTokenProvider.getUserPk(refreshToken);
            List<String> roles = jwtTokenProvider.getRoles(refreshToken);
            accessToken = jwtTokenProvider.generateToken(Uid, roles);
        }else{
            responseService.getFailResult(-1, "유효한토큰이없습니다.");
        }
        return responseService.getSingleResult(accessToken);
    }


}