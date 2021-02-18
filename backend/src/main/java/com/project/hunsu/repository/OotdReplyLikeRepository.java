package com.project.hunsu.repository;

import com.project.hunsu.model.entity.OotdReply;
import com.project.hunsu.model.entity.OotdReplyLike;
import com.project.hunsu.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OotdReplyLikeRepository extends JpaRepository<OotdReplyLike,Long> {

    OotdReplyLike findOotdReplyLikeByOotdReplyAndUser(OotdReply reply, User user);
    List<OotdReplyLike> findOotdReplyLikeByUser(User user);

}
