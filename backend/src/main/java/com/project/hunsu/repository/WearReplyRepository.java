package com.project.hunsu.repository;

import com.project.hunsu.model.entity.Wear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WearReplyRepository extends JpaRepository<Reply,Long> {

    List<Reply> findReplyByWearIdxAndIsActivatedOrderByWriteDate(Wear wear, Boolean isActivated);
    Reply findReplyByIdx(Long idx);
    void deleteReplyByGroupNum(Long groupnum);
    void deleteReplyByIdx(Long idx);

}
