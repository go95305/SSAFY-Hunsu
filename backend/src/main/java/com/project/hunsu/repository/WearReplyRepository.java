package com.project.hunsu.repository;

import com.project.hunsu.model.entity.User;
import com.project.hunsu.model.entity.Wear;
import com.project.hunsu.model.entity.WearReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WearReplyRepository extends JpaRepository<WearReply,Long> {

    List<WearReply> findReplyByWearAndFlagOrderByWriteDate(Wear wear, Boolean isActivated);
    List<WearReply> findReplyByWearOrderByGroupNumAscWriteDateAsc(Wear wear);
    WearReply findReplyByIdx(Long idx);
    List<WearReply> findReplyByUser(User user);

}
