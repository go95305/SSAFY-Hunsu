package com.project.hunsu.repository;

import com.project.hunsu.model.entity.Ootd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OotdRepository extends JpaRepository<Ootd,Long> {


    Ootd findByIdx(Long idx);

    List<Ootd> findAllByOrderByWriteDate();

    List<Ootd> findAllByOrderByCountDesc();

    List<Ootd> findOotdByOrderByWriteDate();

    List<Ootd> findOotdByOrderByCountDesc();
}
