package com.hindsight.authdemo.controller.v1;

import com.hindsight.authdemo.advice.exception.CUserExistException;
import com.hindsight.authdemo.advice.exception.CUserNotFoundException;
import com.hindsight.authdemo.entity.User;
import com.hindsight.authdemo.model.response.CommonResult;
import com.hindsight.authdemo.model.response.SingleResult;
import com.hindsight.authdemo.model.social.KakaoProfile;
import com.hindsight.authdemo.model.social.SocialSignUp;
import com.hindsight.authdemo.repository.UserJpaRepo;
import com.hindsight.authdemo.service.ResponseService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;


@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {

    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService; // 결과를 처리할 Service


    // 회원 추가정보 입력 폼이랑 같다면 그냥 폼으로 받으면 안되나?
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
//    @PutMapping(value = "/user")
//    public SingleResult<User> modify( // 기존 uid 기반이어서 현재 pk 바뀐 후
//            @ApiParam(value = "회원번호", required = true) @RequestParam int msrl,
//            @ApiParam(value = "회원이름", required = true) @RequestParam String nickname) {
//        User user = User.builder()
//                .nickname(nickname)
//                .build();
//        return responseService.getSingleResult(userJpaRepo.save(user));
//    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
    @PutMapping(value = "/user")
    public SingleResult<User> modify(@ApiParam("회원 수정") @RequestBody SocialSignUp form){ //Uid

        User user = User.builder()
                .nickname(String.valueOf(form.getNickname()))
                .age(form.getAge())
                .size(form.getSize())   // 로그인 폼
                .height(form.getHeight())   //로그인 폼
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 삭제", notes = "Uid로 회원정보를 삭제한다")
    @DeleteMapping(value = "/user/{Uid}") //msrl ->Uid
    public CommonResult delete(
            @ApiParam(value = "회원번호", required = true) @PathVariable String Uid) {
//        userJpaRepo.
        // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return responseService.getSuccessResult();
    }

//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "회원 단건 조회", notes = "회원번호(msrl)로 회원을 조회한다")
//    @GetMapping(value = "/user")
////    public SingleResult<User> findUserById(@ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang) {
//    public SingleResult<User> findUserById(@ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang) {
//        // SecurityContext에서 인증받은 회원의 정보를 얻어온다.
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String id = authentication.getName();
//        System.out.println(id);
//        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다./
//        return responseService.getSingleResult(userJpaRepo.findByUid(id));
////        return responseService.getSingleResult(authentication.getDetails().toString());
//    }

    //    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
//    })
//    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다")
//    @GetMapping(value = "/users")
//    public ListResult<User> findAllUser() {
//        // 결과데이터가 여러건인경우 getListResult를 이용해서 결과를 출력한다.
//        return responseService.getListResult(userJpaRepo.findAll());
//    }
}
