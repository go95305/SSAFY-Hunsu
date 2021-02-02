package com.project.hunsu.repository;

import com.project.hunsu.model.entity.OotdLike;
import com.project.hunsu.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OotdLikeRepository extends JpaRepository<OotdLike, Long> {
    OotdLike findOotdLikeByOotdIdxAndUser(Long idx, User user);

//    List<OotdLike> findOotdLikeByOotdIdx(Long idx);

//    OotdLike findByOotdIdxAndNickname(Long ootdIdx, String nickName);
}
