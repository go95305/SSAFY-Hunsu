package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Entity.Ootd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//@Service
//@Transactional
public interface OotdRepository extends JpaRepository<Ootd,Long> {

    List<Ootd> findOotdByOrderByWriteDate();
    List<Ootd> findOotdByOrderByCountDesc();


    Ootd findByIdx(Long idx);

    void deleteByIdx(Long idx);
}
