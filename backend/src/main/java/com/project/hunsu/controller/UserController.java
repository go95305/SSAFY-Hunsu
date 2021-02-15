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
    @ApiOperation(value = "마이페이지", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- nickname(path): 선택된 프로필의 닉네임\n\n" +
            "Response\n" +
            "- myPageDetailDTO\n" +
            "-- mypageNickname: 선택된 프로필의 닉네임\n" +
            "-- mypage: 사용자(페이지 보는사람)의 마이페이지인지 여부\n" +
            "-- uid: 선택된 프로필의 uid\n" +
            "-- follow: 사용자(페이지 보는사람)의 선택된 프로필에 대한 팔로우 버튼 활성화 여부\n" +
            "-- ootd_like_list(Long 배열): 선택된 프로필의 사용자가 좋아요 등록한 ootd의 idx(글번호) 리스트\n" +
            "-- ootd_list(Long 배열): 선택된 프로필의 사용자가 작성한 ootd의 idx(글번호) 리스트\n" +
            "-- follower_list(String): 팔로워 닉네임 리스트\n" +
            "-- following_list(String): 팔로잉 닉네임 리스트\n")
    public MyPageDetailDTO mypage(@RequestHeader("X-AUTH-ACCESS") String jwtToken,  @PathVariable String nickname) {
        String myNickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        MyPageDetailDTO myPageDetailDTO = userService.mypage(myNickname, nickname);

        return myPageDetailDTO;
    }

    @PostMapping("/user/follow")
    @ApiOperation(value = "팔로우 추가/삭제", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- nickname(param): 팔로우할 대상의 닉네임\n\n" +
            "Response\n" +
            "- isActivated: 사용자(페이지 보는사람)의 선택된 프로필에 대한 팔로우 버튼 활성화 여부")
    public boolean userFollowAdd(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @RequestParam String nickname) {
        String myNickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        boolean isActivated = userService.follow(myNickname,nickname);

        return isActivated;
    }

    @GetMapping("/user/mypage/profile")
    @ApiOperation(value = "프로필 수정 페이지", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n\n" +
            "Response\n" +
            "- profileDTO\n" +
            "-- nickname: 사용자(페이지 보는사람)의 닉네임\n" +
            "-- height: 사용자(페이지 보는사람)의 키 정보\n" +
            "-- size: 사용자(페이지 보는사람)의 사이즈 정보\n")
    public ProfileDTO profileValue(@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        ProfileDTO profileDTO = userService.profileValue(nickname);

        return profileDTO;
    }

    //입력: nickname(기존 닉네임), ProfileDTO(nickname, height, size)
    @PutMapping("/user/mypage/modify")
    @ApiOperation(value = "프로필 수정", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n" +
            "- RequestBody\n" +
            "-- nickname: 변경할 닉네임\n" +
            "-- height: 변경할 키 값\n" +
            "-- size: 변경할 사이즈 값\n\n" +
            "Response\n" +
            "- profile\n" +
            "-- nickname: 사용자(페이지 보는사람)의 변경된 닉네임\n" +
            "-- height: 사용자(페이지 보는사람)의 변경된 키\n" +
            "-- size: 사용자(페이지 보는사람)의 변경된 사이즈\n")
    public ProfileDTO profileModify(@RequestHeader("X-AUTH-ACCESS") String jwtToken, @RequestBody ProfileDTO profileDTO) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        ProfileDTO profile = userService.profileModify(nickname, profileDTO);

        return profile;
    }

    //입력: nickname(기존 닉네임)
    @PutMapping("/user/mypage/delete")
    @ApiOperation(value = "회원탈퇴", notes = "Parameter\n" +
            "- jwtToken(RequestHeader)\n\n" +
            "Response(x)")
    public void userDel(@RequestHeader("X-AUTH-ACCESS") String jwtToken) {
        String nickname=userRepository.findUserByJwtAccess(jwtToken).getNickname();
        userService.userDelete(nickname);
    }


}