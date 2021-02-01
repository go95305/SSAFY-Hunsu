package com.project.hunsu.repository;

import com.project.hunsu.model.entity.OotdLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OotdLikeRepository extends JpaRepository<OotdLike, Long> {

//    List<OotdLike> findOotdLikeByOotdIdx(Long idx);

//    OotdLike findByOotdIdxAndNickname(Long ootdIdx, String nickName);
}
