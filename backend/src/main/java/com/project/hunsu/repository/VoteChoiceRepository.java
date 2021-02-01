package com.project.hunsu.repository;

import com.project.hunsu.model.entity.User;
import com.project.hunsu.model.entity.VoteChoice;
import com.project.hunsu.model.entity.VoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteChoiceRepository extends JpaRepository<VoteChoice,Long> {
    VoteChoice findVoteChoiceByVoteItemAndUser(VoteItem voteItem, User user);
    void deleteVoteChoiceByIdx(Long idx);
    void deleteVoteChoiceByVoteItem(VoteItem voteItem);
}
