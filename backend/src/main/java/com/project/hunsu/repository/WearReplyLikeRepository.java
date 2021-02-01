package com.project.hunsu.repository;

import com.project.hunsu.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WearReplyLikeRepository extends JpaRepository<ReplyLike,Long> {
    ReplyLike findReplyLikeByReplyAndUser(Reply reply, User user);
    void deleteReplyLikeByIdx(Long idx);
}
