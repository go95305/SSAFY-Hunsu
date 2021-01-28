package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply,Long> {

    Reply findAllByOotdIdx(Long idx);

    List<Reply> findReplyByOotdIdx(Long idx);
}
