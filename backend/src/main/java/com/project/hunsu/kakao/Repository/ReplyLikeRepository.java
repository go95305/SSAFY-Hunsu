package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Entity.Reply;
import com.project.hunsu.Entity.ReplyLike;
import com.project.hunsu.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike,Long> {
    ReplyLike findReplyLikeByReplyAndUser(Reply reply, User user);
    void deleteReplyLikeByIdx(Long idx);
}
