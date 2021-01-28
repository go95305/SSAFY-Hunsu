package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Entity.User;
import com.project.hunsu.Entity.VoteChoice;
import com.project.hunsu.Entity.VoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteChoiceRepository extends JpaRepository<VoteChoice,Long> {
    VoteChoice findVoteChoiceByVoteItemAndUser(VoteItem voteItem, User user);
    void deleteVoteChoiceByIdx(Long idx);
    void deleteVoteChoiceByVoteItem(VoteItem voteItem);
}
