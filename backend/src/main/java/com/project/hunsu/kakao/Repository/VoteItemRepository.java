package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Entity.User;
import com.project.hunsu.Entity.Vote;
import com.project.hunsu.Entity.VoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteItemRepository extends JpaRepository<VoteItem,Long> {
    List<VoteItem> findVoteItemByVoteOrderByVote(Vote vote);
    VoteItem findVoteItemByIdx(Long idx);
    void deleteVoteItemByVote(Vote vote);
}
