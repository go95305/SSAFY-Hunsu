package com.project.hunsu.repository;

import com.project.hunsu.model.entity.OotdReply;
import com.project.hunsu.model.entity.Wear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OotdReplyRepository extends JpaRepository<OotdReply,Long> {

    OotdReply findReplyByIdx(Long idx);


    List<OotdReply> findReplyByOotdIdx(Long idx);

    List<OotdReply> findReplyByOrderByWriteDate();

    List<OotdReply> findOotdReplyByOotdIdx(Long ootdIdx);

    List<OotdReply> findOotdReplyByOrderByWriteDate();
}
