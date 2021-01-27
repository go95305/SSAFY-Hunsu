package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Entity.Ootd;
import com.project.hunsu.Entity.OotdLike;
import com.project.hunsu.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OotdLikeRepository extends JpaRepository<OotdLike, Long> {

    List<OotdLike> findOotdLikeByOotdIdx(Long idx);
}
