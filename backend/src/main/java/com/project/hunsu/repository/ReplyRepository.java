package com.project.hunsu.repository;

import com.project.hunsu.model.entity.Reply;
import com.project.hunsu.model.entity.Wear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply,Long> {

    Reply findAllByOotdIdx(Long idx);
    List<Reply> findReplyByWearOrderByWriteDate(Wear wear);
    Reply findReplyByIdx(Long idx);
    void deleteReplyByGroupNum(Long groupnum);
    void deleteReplyByIdx(Long idx);

    List<Reply> findReplyByOotdIdx(Long idx);
}
