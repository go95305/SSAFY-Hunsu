package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Entity.Reply;
import com.project.hunsu.Entity.Wear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    Reply findAllByOotdIdx(Long idx);
<<<<<<< HEAD
    List<Reply> findReplyByWearOrderByWriteDate(Wear wear);
    Reply findReplyByIdx(Long idx);
    void deleteReplyByGroupNum(Long groupnum);
    void deleteReplyByIdx(Long idx);
=======

    List<Reply> findReplyByOotdIdx(Long idx);
>>>>>>> 7a923d1ddb8773537fed84617573b8a37d3a7fb4
}
