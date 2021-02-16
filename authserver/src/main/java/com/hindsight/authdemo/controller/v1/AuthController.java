package com.hindsight.authdemo.controller.v1;

import com.hindsight.authdemo.config.security.JwtTokenProvider;
import com.hindsight.authdemo.entity.User;
import com.hindsight.authdemo.model.response.CommonResult;
import com.hindsight.authdemo.model.response.TokenResult;
import com.hindsight.authdemo.model.response.SingleResult;
import com.hindsight.authdemo.model.social.JwtTokenform;
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
import java.util.Map;
import java.util.Optional;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"})
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


    @ApiOperation(value = "kakaoToken",notes = "테스트를 위해 카카오토큰 얻어오기")
    @PostMapping(value="/test")
    public KakaoToken test(@ApiParam(value = "소셜 access_token", required = true) @RequestParam("code") String authorize_code){
        System.out.println(authorize_code);
        KakaoToken tokens = null;
        Map<String, String> map = kakaoService.getAccessToken(authorize_code);
        String accessToken = map.get("accessToken");
        String refreshToken = map.get("refreshToken");
        System.out.println(accessToken);
        System.out.println(refreshToken);
        tokens.setAccessToken(accessToken);
        tokens.setRefreshToken(refreshToken);
        return tokens;
    }

    @ResponseBody
    @ApiOperation(value="회원 체크 (O)", notes="유저 여부 판단(DB에 있는 유저이면 code 1 리턴, 없으면 -1리턴)\n" +
                                                "\n"+
                                                "Parameter \n" +
                                                "- tokens( String AccessToken, String RefreshToken)\n" +
                                                "\n" +
                                                "Response \n" +
                                                "- sucess : 성공여부\n" +
                                                "- code : 1 (토큰값 받아서 로컬스토리지 저장, 로그인), -1( 추가정보 입력 후, form을 /signup 으로 리다이렉트 , 회원가입처리)\n" +
                                                "- msg\n" +
                                                "code 값이 1일 때\n" +
                                                "- jwtAccess : jwt엑세스토큰\n" +
                                                "- jwtRefresh : jwt리프레시토큰\n" +
                                                "- nickname : 닉네임")
    @PostMapping(value="/usercheck")
    public TokenResult usercheck(
            @ApiParam(value = "KakaoToken( String AccessToken, String RefreshToken)", required = true) @RequestBody KakaoToken tokens){
        System.out.println("AccessToken: "+tokens.getAccessToken());
        System.out.println("RefreshToken: "+tokens.getRefreshToken());
        /////////////////////////////////////////////////////////////////

        TokenResult result = new TokenResult();
        ///////////////////////////////////////////////////////////////

        // 카카오 서버에서 받은 엑세스 토큰으로 카카오 프로필 가져오기
        KakaoProfile profile = kakaoService.getKakaoProfile(tokens.getAccessToken());
        System.out.println("profile : " + profile);
        // 프로필의 id로 db에서 사람찾기
        Optional<User> user = Optional.ofNullable(userJpaRepo.findUserByUidAndFlag(profile.getUid(), true));
        System.out.println("User: "+user);
        if(user.isPresent()) { // 있으면
            System.out.println("로그인!!!");
            // 토큰 값들 수정 !!
            String jwtToken=jwtTokenProvider.generateToken(profile.getUid(),user.get().getRoles());
            String jwtRefresh=jwtTokenProvider.generateRefreshToken(profile.getUid(),user.get().getRoles());
            userService.setAllTokens(profile.getUid(),tokens.getAccessToken(),tokens.getAccessToken(),jwtToken,jwtRefresh);
            System.out.println("토큰수정완료");
            /////////////////////////////////////////////////////////////////////////////////
            result.setSuccess(true);
            result.setCode(1);
            result.setMsg("로그인");
            result.setJwtToken(jwtToken);
            result.setJwtRefresh(jwtRefresh);
            result.setNickname(user.get().getNickname());
            result.setUid(user.get().getUid());
                  //로그인
        }else{
            System.out.println("회원가입!!!");
            Long uid = userService.joinUser(profile.getUid(),tokens.getAccessToken(),tokens.getRefreshToken(),false);
            System.out.println("가입완료");
            /////////////////////////////////////////////////////////////////
            result.setSuccess(true);
            result.setCode(-1);
            result.setMsg("회원가입");
//            result.setUid(uid);
//            result.setAccessToken(tokens.getAccessToken());
            System.out.println("SetCode2");
            ///////////////////////////////////////////////////////////////
       // 회원가입
        }
        return result;
    }


    @ApiOperation(value="jwtToken 로그인 (O)",notes ="로컬에 jwt토큰이 있을 때 로그인 프로세스\n" +
                                                    "\n"+
                                                    "Parameter \n" +
                                                    "- tokens( String jwtToken, String jwtRefresh)\n" +
                                                    "\n" +
                                                    "Response \n" +
                                                    "- sucess : 성공여부\n" +
                                                    "- code : 1 (토큰값 받아서 로컬스토리지 저장, 로그인), 0(카카오 로그인페이지로)\n" +
                                                    "- msg\n" +
                                                    "code 값이 1일 때\n" +
                                                    "- jwtAccess : jwt엑세스토큰\n" +
                                                    "- jwtRefresh : jwt리프레시토큰\n" +
                                                    "- nickname : 닉네임")
    @PostMapping(value="/tokenlogin")
    public TokenResult haveJwtToken(@ApiParam("jwtToken") @RequestBody JwtTokenform tokens){
        System.out.println("Check Start!!!!!!!!!!");
        TokenResult result = new TokenResult();
        if(jwtTokenProvider.validateToken(tokens.getJwtToken())){
            System.out.println("jwtToken유효");
            long uid=jwtTokenProvider.getUserPk(tokens.getJwtToken()); // jwtToken을 이용해 uid추출
            User user = userJpaRepo.findUserByUid(uid);
            System.out.println("#############################");
            System.out.println(user.getRoles());
            result.setMsg("자동로그인");
            result.setCode(1);
            result.setSuccess(true);
            result.setJwtToken(tokens.getJwtToken());
            result.setJwtRefresh(tokens.getJwtRefresh());
            result.setNickname(user.getNickname());
            result.setUid(user.getUid());

            return result;

        }else
            System.out.println("jwt토큰유효하지않음");
            System.out.println("RefreshToken을 활용하여 AccessToken 갱신!!");
            if(jwtTokenProvider.validateToken(tokens.getJwtRefresh())){     //리프레시토큰 유효
                System.out.println("jwtRefresh토큰유효");
                long Uid =Long.parseLong(String.valueOf(jwtTokenProvider.getUserPk(tokens.getJwtRefresh()))); //Uid type
                System.out.println("Uid: " + Uid);
                List<String> roles = jwtTokenProvider.getRoles(tokens.getJwtRefresh());
//                System.out.println("#############################");
//                System.out.println(roles);

                String jwtToken = jwtTokenProvider.generateToken(Uid, roles);

                User user = userJpaRepo.findUserByUid(Uid);
//                System.out.println("#############################");
//                System.out.println(user.getRoles());
                userService.updatejwtToken(Uid,jwtToken);

                result.setCode(1);
                result.setMsg("자동로그인(jwt갱신)");
                result.setSuccess(true);
                result.setJwtToken(jwtToken);
                result.setJwtRefresh(tokens.getJwtRefresh());
                result.setNickname(user.getNickname());
                result.setUid(user.getUid());
                return result;

            }else{
                System.out.println("재발급불가");
                result.setCode(0);
                result.setMsg("카카오로그인페이지로");
                result.setSuccess(false);
                return result;
            }
    }

    @ApiOperation(value="소셜 계정 가입 (O)", notes="추가 정보 form과 kakaoAccessToken를 받아와 소셜 계정 회원가입을 한다.\n" +
            "\n" +
            "Parameter\n" +
            "1. [form]\n" +
            "- nickname\n" +
            "- height\n" +
            "- size\n" +
            "\n" +
            "2. String AccessToken\n" +
            "\n" +
            "Respnse\n" +
            "- sucess : 성공여부\n" +
            "- code :1\n" +
            "- msg\n" +
            "- jwtAccess : jwt엑세스토큰\n" +
            "- jwtRefresh : jwt리프레시토큰\n" +
            "- nickname : 닉네임")
    @PostMapping(value ="/signup")
    public TokenResult signup(@ApiParam("회원가입 시 추가입력 form") @RequestBody SocialSignUp form ,@ApiParam("accessToken") @RequestParam String accessToken){
        
        TokenResult result = new TokenResult();
        User user = userJpaRepo.findUserByAccessTokenAndFlag(accessToken,false);

        System.out.println("USER: "+user.getUid());
        String jwtAccess = jwtTokenProvider.generateToken(user.getUid(),user.getRoles());
        String jwtRefresh= jwtTokenProvider.generateRefreshToken(user.getUid(),user.getRoles());
        userService.addUserInfo(user.getUid(),form.getNickname(), form.getHeight(), form.getSize(),jwtRefresh,jwtAccess,true);
        System.out.println("성공!!");
        result.setSuccess(true);
        result.setCode(1);
        result.setMsg("로그인");
        result.setJwtToken(jwtAccess);
        result.setJwtRefresh(jwtRefresh);
        result.setNickname(form.getNickname());
        result.setUid(user.getUid());
        return result;
    }



    @ApiOperation(value="닉네임 체크 (O)", notes="닉네임 중복체크\n" +
            "\n" +
            "Parameter\n" +
            "- String nickname\n" +
            "\n" +
            "Respnse\n" +
            "- True / False\n" +
            "** true : 닉네임 사용가능 , false : 닉네임 사용불가")
    @PostMapping(value ="/nickname")
    public boolean nicknamecheck(@ApiParam("nickname") @RequestParam String nickname){

        Optional<User> user = userJpaRepo.findUserByNickname(nickname);
        if (user.isPresent()){
            return false;
        }else{
            return true;
        }

    }

    @ApiOperation(value = "로그아웃(O)",notes = "jwtToken으로 로그아웃 요청, 성공시 로컬스토리지 토큰 값 날리기\n" +
            "\n" +
            "Parameter\n" +
            "- jwtToken\n" +
            "\n" +
            "Response\n" +
            "- code : (1:성공, 0:실패)\n" +
            "- Success \n" +
            "- msg\n" +
            "\n" +
            "** 성공시 DB에서 해당 유저의 4개 토큰값 null처리,프론트에서는 로컬스토리지에서 날리기")
    @PostMapping(value = "/logout")
    public CommonResult logout(@ApiParam("jwtToken") @RequestParam String jwtToken){

        CommonResult result = new CommonResult();
        Optional<User> user=userJpaRepo.findUserByJwtAccess(jwtToken);

        if(user.isPresent()){
            userService.setAllTokens(user.get().getUid(),null,null,null,null);
            result.setCode(1);
            result.setSuccess(true);
            result.setMsg("로그아웃 성공");
            return result;
        }else {
            result.setCode(0);
            result.setSuccess(false);
            result.setMsg("토큰이 유효하지 않습니다.");
            return result;
        }

    }

    @ApiOperation(value = "닉네임과 토큰으로 UID 얻기(O)",notes = "실채훈에서 사용할 APi\n" +
            "\n" +
            "닉네임으로 UID를 얻는다. \n" +
            "\n" +
            "- msg : UID값\n" +
            "\n")
    @GetMapping(value ="/getuid")
    public CommonResult getUid(@ApiParam("jwtToken") @RequestParam String jwtToken, @ApiParam("nickname") @RequestParam String nickname){
        CommonResult result = new CommonResult();
        Optional<User> reqUser = userJpaRepo.findUserByJwtAccess(jwtToken);
        if(!reqUser.isPresent()){
            result.setSuccess(false);
            result.setMsg("일치하는 jwtToken이 없습니다"); // 테스트 후 삭제
        }else {
            Optional<User> user = userJpaRepo.findUserByNickname(nickname);
            if (user.isPresent()) {
                result.setSuccess(true);
                result.setMsg(String.valueOf(user.get().getUid()));
            } else {
                result.setSuccess(false);
            }
        }

        return result;
    }

}