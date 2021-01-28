package com.project.hunsu.repository;

import com.project.hunsu.model.entity.Ootd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OotdRepository extends JpaRepository<Ootd,Long> {


    Ootd findByIdx(Long idx);

    void deleteByIdx(Long idx);

}
