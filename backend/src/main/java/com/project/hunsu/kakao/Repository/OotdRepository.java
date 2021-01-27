package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Dto.OotdDetail;
import com.project.hunsu.Dto.OotdMain;
import com.project.hunsu.Entity.Ootd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OotdRepository extends JpaRepository<Ootd,Long> {


    OotdDetail findByIdx(Long idx);

    void deleteByIdx(Long idx);

    List<Ootd> findAllByOrderByWriteDateAsc();

    List<Ootd> findAllByOrderByCountDesc();
}
