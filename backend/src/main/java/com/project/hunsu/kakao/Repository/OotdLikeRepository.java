package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Entity.Ootd;
import com.project.hunsu.Entity.OotdLike;
import com.project.hunsu.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OotdLikeRepository extends JpaRepository<OotdLike, Long> {

    List<OotdLike> findOotdLikeByOotdIdx(Long idx);
}
