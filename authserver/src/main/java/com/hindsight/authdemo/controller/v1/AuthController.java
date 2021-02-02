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

    private final Environment env;
    @Value("${spring.url.base}")
    private String baseUrl;

    @Value("${spring.social.kakao.client_id}")
    private String kakaoClientId;

    @Value("${spring.social.kakao.redirect}")
    private String kakaoRedirect;

    // 프론트한테 엑세스 토큰 받는다 -> 엑세스 토큰으로 유저 프로필 받아온다 -> 유저인포를 활용해서 jwt토큰 만들어서 Map으로 리턴 -> 프론트가 받아서 로컬 스토리지 저장
    @ApiOperation(value="소셜 로그인 (O)", notes="소셜 로그인을 한다.")
    @PostMapping(value="/signin")        //value="/signin/{provider}"
    public Map<String, String> signinByProvider(
//            @ApiParam(value = "서비스 제공자 provider", required=true) @PathVariable String provider,
            @ApiParam(value = "소셜 access_token", required = true) @RequestBody String socialAccessToken){

        KakaoProfile profile = kakaoService.getKakaoProfile(socialAccessToken);
//        User user = userJpaRepo.findByOauthIdAndProviderName(String.valueOf(profile.getId()), provider)
//                .orElseThrow(CUserNotFoundException::new);
        User user = userJpaRepo.findUserByUid(String.valueOf(profile.getUid())).orElseThrow(CUserNotFoundException::new);

//        if(user.isFlag()){
//            //true면!!!!!!!!
//
//        }
        //jwt token들 생성
        String apiAccessToken = jwtTokenProvider.generateToken(String.valueOf(user.getUID()),user.getRoles());
        String apiRefreshToken = jwtTokenProvider.generateRefreshToken(String.valueOf(user.getUID()), user.getRoles());
//        String apiAccessToken = jwtTokenProvider.generateToken(String.valueOf(user.getUsername()), user.getRoles());
//        String apiRefreshToken = jwtTokenProvider.generateRefreshToken(String.valueOf(user.getUsername()), user.getRoles());

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put(JwtTokenProvider.ACCESS_TOKEN_NAME, apiAccessToken);
        resultMap.put(JwtTokenProvider.REFRESH_TOKEN_NAME, apiRefreshToken);
//        return responseService.getMapResult(resultMap);
        return resultMap;
    }


    //프론트에서 form 받음 -> form의 accesstoken으로 카카오서버에서 유저info 확인 -> user정보 db에 저장
    @ApiOperation(value="소셜 계정 가입 (O)", notes="소셜 계정 회원가입을 한다.")
    @PostMapping(value ="/signup")
    public CommonResult signupProvider(@ApiParam("소셜 회원가입 객체") @RequestBody SocialSignUp form){ //form 안에 a토큰 r토큰도 함께?
        KakaoProfile profile = kakaoService.getKakaoProfile(form.getAccess_token());

        if(profile == null){
            return responseService.getFailResult(-1100, "카카오 서버에서 일치하 정보를 가져올 수 없음");
        }

//        Optional<User> user = userJpaRepo.findByNicknameAndProviderName(String.valueOf(form.getNickname()), form.getProvider());
        Optional<User> user = userJpaRepo.findUserByUid((String.valueOf(profile.getUid())));
        if(user.isPresent())
            throw new CUserExistException();

        String gender = "man";
        userJpaRepo.save(
                User.builder()
                        .nickname(String.valueOf(form.getNickname()))   // 로그인 폼
                        .gender(gender)   //카카오 프로필 프로퍼티 어케가져옴 ㅡㅡ
                        .access_token(form.getAccess_token())
                        .age(form.getAge())     // 로그인 폼
                        .flag(true)             // 이렇게 해도 되나 ??
                        .size(form.getSize())   // 로그인 폼
                        .UID(profile.getUid())  //카카오 프로필에서 얻어와야함
                        .refresh_token(form.getRefresh_token())
                        .height(form.getHeight())   //로그인 폼
//                        .providerName(form.getProvider())
//                        .oauthId(String.valueOf(profile.getId()))
                        .roles(Collections.singletonList("ROLE_USER"))      //롤 무엇?
                        .build());
        return responseService.getSuccessResult();
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

    @GetMapping(value="/login/kakao")
    public RetKakaoAuth redirectKakao(@RequestParam(value="code", required = true) String code){
        logger.info(code);
        return kakaoService.getKakaoTokenInfo(code);
    }

    @ApiOperation(value="토큰 재발급 (~)", notes="리프레시 토큰으로 액세스 토큰을 재발급 한다.")
    @GetMapping("/newtoken/{refreshToken}")
    public SingleResult<String> getTokenFromRefreshToken(@PathVariable String refreshToken){
        String accessToken = null;

        if(jwtTokenProvider.validateToken(refreshToken)){
            String Uid = jwtTokenProvider.getUserPk(refreshToken);
            List<String> roles = jwtTokenProvider.getRoles(refreshToken);
            accessToken = jwtTokenProvider.generateToken(Uid, roles);
        }
        return responseService.getSingleResult(accessToken);
    }


    // deprecated 추후 자체 로그인 구현할때
//    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
//    @PostMapping(value = "/signin")
//    public SingleResult<String> signin(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String id,
//                                       @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
//        User user = userJpaRepo.findByUid(id).orElseThrow(CEmailSigninFailedException::new);
//        if (!passwordEncoder.matches(password, user.getPassword()))
//            throw new CEmailSigninFailedException();
//
//        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
//
//    }
//
//    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
//    @PostMapping(value = "/signup")
////    public CommonResult signup(@ApiParam(value = "회원ID : 이메일", required = true) @RequestBody String id,
////                               @ApiParam(value = "비밀번호", required = true) @RequestBody String password,
////                               @ApiParam(value = "이름", required = true) @RequestBody String name) {
//    public CommonResult signup(@ApiParam(value = "회원ID : 이메일, 비밀번호, 이름", required = true) @RequestBody UserSignUp info) {
//        userJpaRepo.save(User.builder()
//                .uid(info.getId())
//                .password(passwordEncoder.encode(info.getPassword()))
//                .name(info.getName())
//                .roles(Collections.singletonList("ROLE_USER"))
//                .build());
//        return responseService.getSuccessResult();
//    }

}