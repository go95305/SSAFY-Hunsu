package com.hindsight.authdemo.controller.v1;

import com.hindsight.authdemo.config.security.JwtTokenProvider;
import com.hindsight.authdemo.entity.User;
import com.hindsight.authdemo.model.response.CommonResult;
import com.hindsight.authdemo.model.response.TokenResult;
import com.hindsight.authdemo.model.response.SingleResult;
import com.hindsight.authdemo.model.social.KakaoProfile;
import com.hindsight.authdemo.model.social.KakaoToken;
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

import java.util.HashMap;
import java.util.List;
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


    @ApiOperation(value="kakao 로그인 테스트 URL (O)",notes = "입력해둔 url로 접근\n" +
            "\n" +
            "Parameter : X\n" +
            "\n" +
            "Response : String url (카카오 로그인 화면)")
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
    @ApiOperation(value="회원 체크 (O)", notes="유저 여부 판단(DB에 있는 유저이면 code 1 리턴, 없으면 -1리턴)\n" +
                                                "\n"+
                                                "Parameter \n" +
                                                "- tokens( String AccessToken, String RefreshToken)\n" +
                                                "\n" +
                                                "Response \n" +
                                                "- code:1 (토큰값 받아서 로컬스토리지 저장, 로그인)\n" +
                                                "- code:-1( 추가정보 입력 후, form을 /signup 으로 리다이렉트 , 회원가입처리) ")
    @PostMapping(value="/usercheck")
    public TokenResult usercheck(
            @ApiParam(value = "엑세스토큰", required = true) @RequestBody KakaoToken tokens){
        System.out.println("AccessToken: "+tokens.getAccessToken());
        System.out.println("RefreshToken: "+tokens.getRefreshToken());
        /////////////////////////////////////////////////////////////////

        TokenResult result = new TokenResult();
        ///////////////////////////////////////////////////////////////

        // 카카오 서버에서 받은 엑세스 토큰으로 카카오 프로필 가져오기
        KakaoProfile profile = kakaoService.getKakaoProfile(tokens.getAccessToken());
        System.out.println("profile : " + profile);
        // 프로필의 id로 db에서 사람찾기
        Optional<User> user = Optional.ofNullable(userJpaRepo.findUserByUidAndFlag(profile.getUid(), false));
        System.out.println("User: "+user);
        if(user.isPresent()) { // 있으면
            System.out.println("로그인!!!");
            // 토큰 값들 수정 !!
            String jwtToken=jwtTokenProvider.generateToken(profile.getUid(),user.get().getRoles());
            String jwtRefresh=jwtTokenProvider.generateRefreshToken(profile.getUid(),user.get().getRoles());
            userService.setAllTokens(profile.getUid(),tokens.getAccessToken(),tokens.getAccessToken(),jwtRefresh,jwtToken);
            System.out.println("토큰수정완료");
            /////////////////////////////////////////////////////////////////////////////////
            result.setSuccess(true);
            result.setCode(1);
            result.setMsg("로그인");
            result.setAccessToken(jwtToken);
            result.setRefreshToken(jwtRefresh);
                  //로그인
        }else{
            System.out.println("회원가입!!!");
            userService.joinUser(profile.getUid(),tokens.getAccessToken(),tokens.getRefreshToken(),false);
            System.out.println("가입완료");
            /////////////////////////////////////////////////////////////////
            result.setSuccess(true);
            result.setCode(-1);
            result.setMsg("회원가입");
            result.setAccessToken(tokens.getAccessToken());
            System.out.println("SetCode2");
            ///////////////////////////////////////////////////////////////


       // 회원가입
        }
        return result;
    }

    @ApiOperation(value="소셜 로그인 (O)", notes="Front에 jwtAccessToken이 있고, 그것이 유효할 때 회원가입을 검증하는 단계\n" +
                                                "\n" +
                                                "Parameter \n" +
                                                "- jwtAccessToken\n" +
                                                "\n" +
                                                "Response\n" +
                                                "- success : 성공여부\n" +
                                                "- code : 성공시 1(로그인처리), 실패시 0(카카오 로그인페이지)\n" +
                                                "- msg : \n" +
                                                "- accessToken : jwtAccessToken\n" +
                                                "- refreshToken : jwtRefreshToken")
    @PostMapping(value="/login")
    public TokenResult login(
            @ApiParam(value = "jwtAccessToken", required = true) @RequestParam String jwtToken){
            TokenResult result = new TokenResult();

            long uid=jwtTokenProvider.getUserPk(jwtToken); // uid추출
            Optional<User> user = userJpaRepo.findUserByJwtAccessAndUid(jwtToken,uid);
            if (user.isPresent()){
                result.setCode(1);
                result.setMsg("로그인");
                result.setSuccess(true);
                result.setAccessToken(jwtToken);
                result.setRefreshToken(user.get().getRefreshToken());
                return result;
            }else{
                result.setCode(0);
                result.setMsg("카카오로그인페이지로");
                result.setSuccess(false);
                return result;
            }

//        if(jwtTokenProvider.validateToken(jwtToken)){    // jwt리프레시토큰이 유효할 때
//            Optional<User> user = userJpaRepo.findUserByJwtAccess(jwtToken);
//            System.out.println("USER :" + user.get().getUid());
//            String kakaoRefresh=user.get().getRefreshToken();
//            // 카카오 리프레시 토큰 활용해서 카카오 엑세스 토큰 요청
//            String kakaoAccess=kakaoService.reKakaoAccessToken(kakaoRefresh);
//            if(!jwtTokenProvider.validateToken(kakaoRefresh)){  // 카카오 리프레시가 유효하지 않으면
//                return "fail";                                  // 실패했다고 리턴
//            }else{                                              // 유효하면
//                //카카오 엑세스 토큰 저장
//                userService.updateAccessToken(user.get().getUid(),kakaoAccess);
//                //jwtRefreshToken을 통해 jwtAccessToken생성 후 프론트에 전달
//                String jwtAccessToken = jwtTokenProvider.generateToken(user.get().getUid(),user.get().getRoles());
//                System.out.println(jwtAccessToken);
//
//                return  jwtAccessToken;
//            }
//        }else{
//            return "fail";
//        }

    }


    @ApiOperation(value="소셜 계정 가입 (O)", notes="추가 정보 form를 받아와 소셜 계정 회원가입을 한다.\n" +
            "\n" +
            "Parameter\n" +
            "[form]\n" +
            "- Uid : 유저id\n" +
            "- nickname\n" +
            "- height\n" +
            "- size\n" +
            "- AccessToken\n" +
            "\n" +
            "Respnse(성공시에만)\n" +
            "- sucess : 성공여부\n" +
            "- code :0\n" +
            "- msg\n" +
            "")
    @PostMapping(value ="/signup")
    public TokenResult signupProvider(@ApiParam("소셜 회원가입 객체") @RequestBody SocialSignUp form){
        System.out.println("Form uid: "+form.getAccessToken());
        TokenResult result = new TokenResult();
        User user = userJpaRepo.findUserByAccessTokenAndFlag(form.getAccessToken(),false);

        System.out.println("USER: "+user.getUid());
        String jwtAccess = jwtTokenProvider.generateToken(user.getUid(),user.getRoles());
        String jwtRefresh= jwtTokenProvider.generateRefreshToken(user.getUid(),user.getRoles());
        userService.addUserInfo(user.getUid(),form.getNickname(), form.getHeight(), form.getSize(),jwtRefresh,jwtAccess,true);
        System.out.println("성공!!");
        result.setSuccess(true);
        result.setCode(1);
        result.setMsg("토큰을 받고 로그인처리 하세요");
        result.setAccessToken(jwtAccess);
        result.setRefreshToken(jwtRefresh);
        return result;
    }

    @ApiOperation(value="엑세스 토큰 검증 (O)",notes = "jwt엑세스토큰 검증\n" +
            "\n" +
            "Parameter \n" +
            "- jwtAccessToken\n" +
            "\n" +
            "Response: true(유효)/false(유효하지않음)\n" +
            "- true : 회원가입검증 (/v1/auth/login 으로 go!!)\n" +
            "- false: v1/auth/newtoken 으로 jwtrefresh 보내기 (jwtrefreshToken을 활용해서 jwtAccessToken을 갱신하기위해)")
    @GetMapping(value="/check")
    public boolean checkToken(@ApiParam("검증할 jwt엑세스토큰") @RequestParam String jwtToken){
        System.out.println("Check Start!!!!!!!!!!");
        if(jwtTokenProvider.validateToken(jwtToken)){
            System.out.println("유효");
            return true;
        }else
            System.out.println("유효하지않음");
            return false;   // 토큰 재발급으로
    }

    @ApiOperation(value="토큰 재발급 (O)", notes="JWT 리프레시 토큰을 받아 액세스 토큰을 재발급 한다.\n" +
            "\n" +
            "Parameter\n" +
            "- jwtRefreshToken\n" +
            "\n" +
            "Response \n" +
            " data : { success : 성공여부\n" +
            "          code : 0 or -1\n" +
            "           msg : 성공하였습니다/유효한토큰이 없습니다.\n" +
            "          data : JWTAccessToken\n" +
            "\n" +
            " ** 입력받은 JWTRefresh토큰이 유효하면 code=0, data 값 확인\n" +
            "    유효하지 않으면 code=-1, v1/auth/url로 go (카카로 로그인페이지)")
    @GetMapping("/newtoken")
    public SingleResult<CommonResult> getTokenFromRefreshToken(@ApiParam("jwtRefrshToken") @RequestParam String jwtRefresh){
//        String result = null;
        CommonResult res;
        System.out.println("RefreshToken을 활용하여 AccessToken 갱신!!");
        if(jwtTokenProvider.validateToken(jwtRefresh)){
            long Uid =Long.parseLong(String.valueOf(jwtTokenProvider.getUserPk(jwtRefresh))); //Uid type
            System.out.println("Uid: " + Uid);
            List<String> roles = jwtTokenProvider.getRoles(jwtRefresh);
            String result = jwtTokenProvider.generateToken(Uid, roles);
            res =responseService.getSingleResult(result);
        }else{
            System.out.println("재발급불가");
//            result=null;
            res= responseService.getFailResult(-1, "유효한토큰이없습니다.");
        }
        return responseService.getSingleResult(res);
    }


}