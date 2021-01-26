package com.project.hunsu.kakao.Repository;

import com.project.hunsu.dto.Ootd;
import com.project.hunsu.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//@Service
//@Transactional
public interface OotdRepository extends JpaRepository<Ootd,Long> {

    List<Ootd> findOotdByOrderByWriteDate();
    List<Ootd> findOotdByOrderByCountDesc();

    Ootd findOotdByIdx(Long idx);

}
