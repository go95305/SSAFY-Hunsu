package com.project.hunsu.repository;

import com.project.hunsu.model.entity.Vote;
import com.project.hunsu.model.entity.VoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteItemRepository extends JpaRepository<VoteItem,Long> {
    List<VoteItem> findVoteItemByVoteOrderByVote(Vote vote);
    VoteItem findVoteItemByIdx(Long idx);

}
