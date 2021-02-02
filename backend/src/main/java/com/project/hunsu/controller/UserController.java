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
    @GetMapping("/user/mypage")
    @ApiOperation(value = "마이페이지 (O)", notes = "마이페이지에서 필요한 값들을 반환")
    public MyPageDetailDTO userFollowAdd(@RequestBody MyPageDTO myPageDTO) {
        MyPageDetailDTO myPageDetailDTO = userService.mypage(myPageDTO.getMyNickname(), myPageDTO.getClickNickname());

        return myPageDetailDTO;
    }

    //입력: myNickname, yourNickname
    @PostMapping("/user/follow")
    @ApiOperation(value = "팔로우 추가/삭제 (O)", notes = "상대방을 팔로우/팔로우해제 하는 기능")
    public boolean userFollowAdd(@RequestBody UserFollowDTO userFollowDTO) {
        boolean isActivated = userService.follow(userFollowDTO.getMyNickname(), userFollowDTO.getYourNickname());

        return isActivated;
    }

    //입력: nickname
    @GetMapping("/user/mypage/profile/{nickname}")
    @ApiOperation(value = "프로필 수정 페이지", notes = "프로필 수정 컴포넌트 이동시 필요한 값들 반환")
    public ProfileDTO profileValue(@PathVariable String nickname) {
        ProfileDTO profileDTO = userService.profileValue(nickname);

        return profileDTO;
    }

    //수정 필요(cascade)
    //입력: nickname(기존 닉네임), ProfileDTO(nickname, gender, height, size)
    @PutMapping("/user/mypage/modify/{nickname}")
    @ApiOperation(value = "프로필 수정", notes = "프로필 수정하는 기능")
    public void profileModify(@PathVariable String nickname, @RequestBody ProfileDTO profileDTO) {
        userService.profileModify(nickname, profileDTO);
    }
    
    //수정 필요(cascade)
    //입력: nickname(기존 닉네임)
    @PutMapping("/user/mypage/delete/{nickname}")
    @ApiOperation(value = "회원탈퇴", notes = "회원탈퇴하는 기능")
    public void profileModify(@PathVariable String nickname) {
        userService.userDelete(nickname);
    }


}