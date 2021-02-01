package com.project.hunsu.repository;

import com.project.hunsu.model.entity.OotdReply;
import com.project.hunsu.model.entity.OotdReplyLike;
import com.project.hunsu.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OotdReplyLikeRepository extends JpaRepository<OotdReplyLike,Long> {
//    OotdReplyLike findReplyLikeByReplyAndUser(OotdReply reply, User user);
//    void deleteReplyLikeByIdx(Long idx);
}
