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
@CrossOrigin(origins = {"http://localhost:3000"})
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

    @ApiOperation(value="소셜 로그인 (O)", notes="소셜 로그인을 한다.")
    @PostMapping(value="/signin/{provider}")
    public Map<String, String> signinByProvider(
            @ApiParam(value = "서비스 제공자 provider", required=true) @PathVariable String provider,
            @ApiParam(value = "소셜 access_token", required = true) @RequestBody String socialAccessToken){

        KakaoProfile profile = kakaoService.getKakaoProfile(socialAccessToken);
        User user = userJpaRepo.findByOauthIdAndProviderName(String.valueOf(profile.getId()), provider)
                .orElseThrow(CUserNotFoundException::new);
        //jwt token들 생성
        String apiAccessToken = jwtTokenProvider.generateToken(String.valueOf(user.getUsername()), user.getRoles());
        String apiRefreshToken = jwtTokenProvider.generateRefreshToken(String.valueOf(user.getUsername()), user.getRoles());

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put(JwtTokenProvider.ACCESS_TOKEN_NAME, apiAccessToken);
        resultMap.put(JwtTokenProvider.REFRESH_TOKEN_NAME, apiRefreshToken);
//        return responseService.getMapResult(resultMap);
        return resultMap;
    }


    @ApiOperation(value="소셜 계정 가입 (O)", notes="소셜 계정 회원가입을 한다.")
    @PostMapping(value ="/signup")
    public CommonResult signupProvider(@ApiParam("소셜 회원가입 객체") @RequestBody SocialSignUp form){
        KakaoProfile profile = kakaoService.getKakaoProfile(form.getAccessToken());
        if(profile == null){
            return responseService.getFailResult(-1100, "카카오 서버에서 일치하 정보를 가져올 수 없음");
        }
        Optional<User> user = userJpaRepo.findByNicknameAndProviderName(String.valueOf(form.getNickname()), form.getProvider());
        if(user.isPresent())
            throw new CUserExistException();

        userJpaRepo.save(
                User.builder()
                        .nickname(String.valueOf(form.getNickname()))
                        .providerName(form.getProvider())
                        .oauthId(String.valueOf(profile.getId()))
                        .roles(Collections.singletonList("ROLE_USER"))
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
            String username = jwtTokenProvider.getUserPk(refreshToken);
            List<String> roles = jwtTokenProvider.getRoles(refreshToken);
            accessToken = jwtTokenProvider.generateToken(username, roles);
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