package com.project.chat.repository;

import com.project.chat.controller.ChatController;
import com.project.chat.dto.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChatRoomRepository {
    private final static Logger LOG = LoggerFactory.getLogger(ChatController.class);
    // Redis CacheKeys
    private static final String CHAT_ROOMS = "CHAT_ROOM"; // 채팅룸 저장
    public static final String USER_COUNT = "USER_COUNT"; // 채팅룸에 입장한 클라이언트수 저장
    public static final String LIKE_COUNT = "LIKE_COUNT"; // 채팅룸에 입장한 클라이언트수 저장
    public static final String ENTER_INFO = "ENTER_INFO"; // 채팅룸에 입장한 클라이언트의 sessionId와 채팅룸 id를 맵핑한 정보 저장

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, ChatRoom> hashOpsChatRoom;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOpsEnterInfo;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;

    // 모든 채팅방 조회
    public List<ChatRoom> findAllRoom(int sort) {
        List<ChatRoom> chatRoomList = hashOpsChatRoom.values(CHAT_ROOMS);
        //최신순으로 정렬
        if(sort==0) {
            Collections.sort(chatRoomList, new Comparator<ChatRoom>() {
                @Override
                public int compare(ChatRoom o1, ChatRoom o2) {
                    if (o1.getCreatDate() > o2.getCreatDate()) {
                        return -1;
                    } else if (o1.getCreatDate() < o2.getCreatDate()) {
                        return 1;
                    }
                    return 0;
                }
            });
        }
        //인기순으로 정렬
        else{
            Collections.sort(chatRoomList, new Comparator<ChatRoom>() {
                @Override
                public int compare(ChatRoom o1, ChatRoom o2) {
                    if (o1.getLikeCount() > o2.getLikeCount()) {
                        return -1;
                    } else if (o1.getLikeCount() < o2.getLikeCount()) {
                        return 1;
                    }
                    return 0;
                }
            });
        }
        return chatRoomList;
    }

    // 특정 채팅방 조회
    public ChatRoom findRoomById(String id) {
        return hashOpsChatRoom.get(CHAT_ROOMS, id);
    }

    // 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
    public ChatRoom createChatRoom(ChatRoom newRoom) {
        ChatRoom chatRoom = ChatRoom.create(newRoom.getName(), newRoom.getPublisher(), newRoom.getHashtagList(), newRoom.getFixedComment());
        hashOpsChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    public void updateLikeCount(String roomId) {
        ChatRoom chatRoom = hashOpsChatRoom.get(CHAT_ROOMS, roomId);
        assert chatRoom != null;
        chatRoom.setLikeCount(chatRoom.getLikeCount() + 1);
//        hashOpsChatRoom.delete(CHAT_ROOMS,roomId);
        hashOpsChatRoom.put(CHAT_ROOMS, roomId, chatRoom);
        LOG.info("uplike" + chatRoom.getLikeCount());
    }

    //채팅방 삭제
    public long removeRoom(String roomId) {
        return hashOpsChatRoom.delete(CHAT_ROOMS, roomId);
    }

    // 유저가 입장한 채팅방ID와 유저 세션ID 맵핑 정보 저장
    public void setUserEnterInfo(String sessionId, String roomId) {
        hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }

    // 유저 세션으로 입장해 있는 채팅방 ID 조회
    public String getUserEnterRoomId(String sessionId) {
        return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // 유저 세션정보와 맵핑된 채팅방ID 삭제
    public void removeUserEnterInfo(String sessionId) {
        hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }

    // 채팅방 유저수 조회
    public long getUserCount(String roomId) {
        return Long.parseLong(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
    }

    //채팅방 좋아요 조회
    public Long getLikeCount(String roomId) {//메인 페이지에 계속 0인 이유가 orElse부분인듯?
        return Long.parseLong(Optional.ofNullable(valueOps.get(LIKE_COUNT + "_" + roomId)).orElse("0"));
    }


    // 채팅방에 입장한 유저수 +1
    public long plusUserCount(String roomId) {
        return Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + roomId)).orElse(0L);
    }

    //채팅방 좋아요 +1
    public Long plusLikeCount(String roomId) {
        updateLikeCount(roomId);
        return Optional.ofNullable(valueOps.increment(LIKE_COUNT + "_" + roomId)).orElse(0L);
    }

    // 채팅방에 입장한 유저수 -1
    public long minusUserCount(String roomId) {
        return Optional.ofNullable(valueOps.decrement(USER_COUNT + "_" + roomId)).filter(count -> count > 0).orElse(0L);
    }
}