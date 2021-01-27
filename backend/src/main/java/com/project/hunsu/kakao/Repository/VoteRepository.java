package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote,Long> {
    Vote findVoteByWearIdx(Long idx);
}
