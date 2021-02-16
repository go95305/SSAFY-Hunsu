package com.project.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId; //채팅방 ID
    private String title; //채팅방 이름
    private long userCount; // 채팅방 인원수
    private Long likeCount; //채팅방 좋아요 수
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> hashtagList; //해시태그
    private String nickname;//개설자
    private Long uid; //개설자 uid
    private String jwtToken;
    private String fixedComment; //고정댓글
    private Long creatDate; // 채팅방 개설 시간(날짜)

    public static ChatRoom create(String title, String nickname, List<String> hashtagList, String fixedComment, Long uid) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.title = title;
        chatRoom.nickname = nickname;
        chatRoom.hashtagList = new ArrayList<>();
        chatRoom.hashtagList = hashtagList;
        chatRoom.fixedComment = fixedComment;
        chatRoom.creatDate = System.currentTimeMillis();
        chatRoom.likeCount = (long)0;
        chatRoom.uid = uid;

        return chatRoom;
    }
}