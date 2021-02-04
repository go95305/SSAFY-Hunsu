package com.project.hunsu.controller;

import com.project.hunsu.model.dto.*;
import com.project.hunsu.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = {"*"})
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //입력: myNickname, clickNickname
    @GetMapping("/user/mypage/{myNickname}/{clickNickname}")
    @ApiOperation(value = "마이페이지 (O)", notes = "\n\n" +
            "Parameter\n" +
            "-myNickname(Path): 현재 사용자의 닉네임\n" +
            "-clickNickname(Path): 보고자 하는 마이페이지 대상의 닉네임\n" +
            "Response\n" +
            "-mypageNickname: 마이페이지의 대상 닉네임\n" +
            "-mypage: 내 마이페이지인지 여부(true or false)\n" +
            "-ootd_like_list(num): 좋아요 등록된 ootd의 idx 리스트\n" +
            "-ootd_list(num): {clickNickname}가 작성한 ootd의 idx 리스트\n" +
            "-follow: 팔로우 버튼 활성화 여부(true or false)\n" +
            "-follower_list(String): 팔로워 닉네임 리스트\n" +
            "-following_list(String): 팔로잉 닉네임 리스트")
    public MyPageDetailDTO userFollowAdd(@PathVariable String myNickname, @PathVariable String clickNickname) {
        MyPageDetailDTO myPageDetailDTO = userService.mypage(myNickname, clickNickname);

        return myPageDetailDTO;
    }

    //입력: myNickname, yourNickname
    @PostMapping("/user/follow")
    @ApiOperation(value = "팔로우 추가/삭제 (O)", notes = "Parameter\n" +
            "-myNickname: 현재 사용자의 닉네임\n" +
            "-yourNickname: 팔로우 하고자 하는 대상의 닉네임\n" +
            "Response\n" +
            "-isActivated: 팔로우 버튼 활성화 여부(true or false)")
    public boolean userFollowAdd(@RequestBody UserFollowDTO userFollowDTO) {
        boolean isActivated = userService.follow(userFollowDTO.getMyNickname(), userFollowDTO.getYourNickname());

        return isActivated;
    }

    //입력: nickname
    @GetMapping("/user/mypage/profile/{nickname}")
    @ApiOperation(value = "프로필 수정 페이지", notes = "Parameter\n" +
            "-nickname: 수정할 페이지의 닉네임\n" +
            "Response\n" +
            "-nickname: 수정할 페이지의 닉네임\n" +
            "-gender: 위 닉네임의 성별 정보\n" +
            "-height: 위 닉네임의 키 정보\n" +
            "-size: 위 닉네임의 사이즈 정보")
    public ProfileDTO profileValue(@PathVariable String nickname) {
        ProfileDTO profileDTO = userService.profileValue(nickname);

        return profileDTO;
    }

    //입력: nickname(기존 닉네임), ProfileDTO(nickname, gender, height, size)
    @PutMapping("/user/mypage/modify/{nickname}")
    @ApiOperation(value = "프로필 수정", notes = "Parameter\n" +
            "-nickname(Path): 수정전의 닉네임\n" +
            "-nickname: 수정후의 닉네임\n" +
            "-height: 수정후의 키\n" +
            "-size: 수정 후의 사이트\n" +
            "Response(x)")
    public void profileModify(@PathVariable String nickname, @RequestBody ProfileDTO profileDTO) {
        userService.profileModify(nickname, profileDTO);
    }

    //입력: nickname(기존 닉네임)
    @PutMapping("/user/mypage/delete/{nickname}")
    @ApiOperation(value = "회원탈퇴", notes = "Parameter\n" +
            "-nickname: 탈퇴하는 계정(자신)의 닉네임\n" +
            "Response(x)")
    public void profileModify(@PathVariable String nickname) {
        userService.userDelete(nickname);
    }


}