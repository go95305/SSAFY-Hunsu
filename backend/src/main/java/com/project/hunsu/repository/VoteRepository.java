package com.project.hunsu.repository;

import com.project.hunsu.model.entity.Vote;
import com.project.hunsu.model.entity.Wear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote,Long> {
    Vote findVoteByWear(Wear wear);
    void deleteVoteByWear(Wear wear);
}
