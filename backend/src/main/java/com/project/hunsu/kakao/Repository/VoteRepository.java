package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Entity.Vote;
import com.project.hunsu.Entity.Wear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote,Long> {
    Vote findVoteByWear(Wear wear);
    void deleteVoteByWear(Wear wear);
}
