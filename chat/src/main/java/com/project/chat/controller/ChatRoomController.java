package com.project.chat.controller;

import com.project.chat.dto.ChatRoom;
import com.project.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllRoom();
        chatRooms.stream().forEach(room -> room.setUserCount(chatRoomRepository.getUserCount(room.getRoomId())));
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


}