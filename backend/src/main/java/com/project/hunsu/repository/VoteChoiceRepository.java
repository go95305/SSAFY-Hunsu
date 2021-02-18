package com.project.hunsu.repository;

import com.project.hunsu.model.entity.User;
import com.project.hunsu.model.entity.VoteChoice;
import com.project.hunsu.model.entity.VoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteChoiceRepository extends JpaRepository<VoteChoice,Long> {
    VoteChoice findVoteChoiceByVoteItemAndUser(VoteItem voteItem, User user);
    List<VoteChoice> findVoteChoiceByUser(User user);

}
