package com.project.chat.controller;

import com.project.chat.dto.ChatRoom;
import com.project.chat.dto.JSONResponseUtil;
import com.project.chat.dto.UidResponse;
import com.project.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000","http://i4c102.p.ssafy.io"})
@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    //실채훈 메인페이지(모든 채팅방 리스트)
    @GetMapping("/rooms/{sort}")
    @ResponseBody
    public List<ChatRoom> room(@PathVariable int sort) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllRoom(sort);
        chatRooms.stream().forEach(room -> room.setUserCount(chatRoomRepository.getUserCount(room.getRoomId())));
        chatRooms.stream().forEach(room -> room.setLikeCount(chatRoomRepository.getLikeCount(room.getRoomId())));
        return chatRooms;
    }

    //채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ChatRoom createRoom(@RequestBody ChatRoom chatRoom) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://i4c102.p.ssafy.io:8081/api/v1/auth/getuid";
        HttpHeaders headers = new HttpHeaders();

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("jwtToken", chatRoom.getJwtToken())
                .queryParam("nickname", chatRoom.getNickname())
                .build(false);    //자동으로 encode해주는 것을 막기 위해 false
        ResponseEntity<String> res = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<String>(headers), String.class);
        chatRoom.setUid(Long.parseLong(res.getBody().split("\"")[7]));

        return chatRoomRepository.createChatRoom(chatRoom);
    }

    //채팅방 입장
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }

    //채팅방 정보 리턴
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }

    //채팅방 삭제
    @PostMapping("/room/remove/{roomId}")
    @ResponseBody
    public void removeRoom(@PathVariable String roomId) {
        long result = chatRoomRepository.removeRoom(roomId);
        log.info("삭제여부" + result);
    }

    @PostMapping("/room/like/{roomId}")
    @ResponseBody
    public long roomlike(@PathVariable String roomId) {
        return chatRoomRepository.plusLikeCount(roomId);
    }


}