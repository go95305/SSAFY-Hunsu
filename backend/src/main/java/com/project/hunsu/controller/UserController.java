package com.project.hunsu.controller;

import com.project.hunsu.model.dto.UserFollowDTO;
import com.project.hunsu.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = { "http://i4c102.p.ssafy.io:3000/" })
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/follow")
    @ApiOperation(value = "팔로우 추가 (O)", notes = "상대방을 팔로우 하는 기능")
    public void userFollowAdd(@Valid @RequestBody UserFollowDTO userFollowDTO) {
        userService.followAdd(userFollowDTO.getMyNickname(), userFollowDTO.getYourNickname());

    }

    @DeleteMapping("/user/follow")
    @ApiOperation(value = "팔로우 삭제 (O)", notes = "팔로우 했단 상대방을 지우는 기능")
    public void userFollowDelete(@Valid @RequestBody UserFollowDTO userFollowDTO) {
        userService.followDelete(userFollowDTO.getMyNickname(), userFollowDTO.getYourNickname());

    }

//    @GetMapping("/user/ootd/{nickname}")
//    @ApiOperation(value = "상대 마이페이지로 이동", notes = "상대 이름 클릭시 상대방 정보를 리턴해준다.")
//    public UserDetail otherUserPage(@PathVariable String nickname) {
//        OotdDetailDTO ootdDetailDTO = new OotdDetailDTO();
//
//        return
//    }


}
