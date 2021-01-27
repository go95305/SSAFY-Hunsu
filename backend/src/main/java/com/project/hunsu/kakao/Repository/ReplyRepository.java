package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    Reply findAllByOotdIdx(Long idx);
    List<Reply> findReplyByWearIdxOrderByWriteDate(Long idx);
    Reply findReplyByIdx(Long idx);
    void deleteReplyByIdx(Long idx);
}
