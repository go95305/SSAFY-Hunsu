package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Entity.OotdLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OotdLikeRepository extends JpaRepository<OotdLike,Long> {

    void deleteByIdxAndNickname(Long ootdIdx, String nickname);

}
