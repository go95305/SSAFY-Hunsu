package com.project.chat.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.chat.controller.ChatController;
import com.project.chat.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Slf4j
@CrossOrigin(origins = {"localhost:3000","i4c102.p.ssafy.io"})
@RequiredArgsConstructor
@Service
public class RedisSubscriber {
    private final static Logger LOG = LoggerFactory.getLogger(ChatController.class);
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;//연결된 클라이언트에게 메시지를 보내고 싶을때

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다.
     */
    public void sendMessage(String publishMessage) {
        try {
//            LOG.info("hihihihihi");
            // ChatMessage 객채로 맵핑
            ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            // 채팅방을 구독한 클라이언트에게 메시지 발송
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }
}