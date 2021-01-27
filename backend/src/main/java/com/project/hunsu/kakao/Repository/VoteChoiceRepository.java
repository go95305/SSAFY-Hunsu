package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Entity.User;
import com.project.hunsu.Entity.VoteChoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteChoiceRepository extends JpaRepository<VoteChoice,Long> {
    VoteChoice findVoteChoicByVoteItemIdxAndUser(Long idx, User user);
    /////수정 필요
}
