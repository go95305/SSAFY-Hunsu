package com.project.hunsu.controller;

import com.project.hunsu.model.dto.*;
import com.project.hunsu.repository.UserRepository;
import com.project.hunsu.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"*"})
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/mypage/{nickname}")
    @ApiOperation(value = "마이페이지 (프론트 수정 필요: uri수정도)", notes = "\n\n" +
            "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "- nickname(Path): 보고자 하는 마이페이지 대상의 닉네임      --수정\n" +
            "Response\n" +
            "- mypageNickname: 마이페이지의 대상 닉네임\n" +
            "- mypage: 내 마이페이지인지 여부(true or false)\n" +
            "- ootd_like_list(num): 좋아요 등록된 ootd의 idx 리스트\n" +
            "-ootd_list(num): {clickNickname}가 작성한 ootd의 idx 리스트\n" +
            "-follow: 팔로우 버튼 활성화 여부(true or false)\n" +
            "-follower_list(String): 팔로워 닉네임 리스트\n" +
            "-following_list(String): 팔로잉 닉네임 리스트")
    public MyPageDetailDTO mypage(@RequestHeader("X-AUTH-ACCESS") String jwtToken,  @PathVariable String nickname) {
        String myNickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        MyPageDetailDTO myPageDetailDTO = userService.mypage(myNickname, nickname);

        return myPageDetailDTO;
    }

    @PostMapping("/user/follow")
    @ApiOperation(value = "팔로우 추가/삭제 (프론트 수정 필요: uri수정도)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "-nickname: 팔로우 하고자 하는 대상의 닉네임      --수정(path->requestbody)\n" +
            "Response\n" +
            "-isActivated: 팔로우 버튼 활성화 여부(true or false)")
    public boolean userFollowAdd(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @RequestBody String nickname) {
        String myNickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        boolean isActivated = userService.follow(myNickname,nickname);

        return isActivated;
    }

    @GetMapping("/user/mypage/profile")
    @ApiOperation(value = "프로필 수정 페이지 (프론트 수정 필요: uri수정도)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "Response\n" +
            "-nickname: 수정할 페이지의 닉네임\n" +
            "-height: 위 닉네임의 키 정보\n" +
            "-size: 위 닉네임의 사이즈 정보")
    public ProfileDTO profileValue(@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        ProfileDTO profileDTO = userService.profileValue(nickname);

        return profileDTO;
    }

    //입력: nickname(기존 닉네임), ProfileDTO(nickname, height, size)
    @PutMapping("/user/mypage/modify")
    @ApiOperation(value = "프로필 수정 (프론트 수정 필요: uri수정도)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "- profileDTO\n" +
            "----- nickname: 수정후의 닉네임\n" +
            "----- height: 수정후의 키\n" +
            "----- size: 수정 후의 사이트\n" +
            "Response(x)")
    public ProfileDTO profileModify(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @RequestBody ProfileDTO profileDTO) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        ProfileDTO profile = userService.profileModify(nickname, profileDTO);

        return profile;
    }

    //입력: nickname(기존 닉네임)
    @PutMapping("/user/mypage/delete")
    @ApiOperation(value = "회원탈퇴 (프론트 수정 필요: uri수정도)", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)      --수정\n" +
            "Response(x)")
    public void userDel(@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        userService.userDelete(nickname);
    }


}