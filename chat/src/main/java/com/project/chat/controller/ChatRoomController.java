package com.project.chat.controller;

import com.project.chat.dto.ChatRoom;
import com.project.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000","http://i4c102.p.ssafy.io"})
@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/room")
    public String rooms() {
        return "/chat/room";
    }

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
    public ChatRoom createRoom(@RequestBody ChatRoom chatRoom) {
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