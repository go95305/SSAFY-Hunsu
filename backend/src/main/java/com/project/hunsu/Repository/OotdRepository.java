package com.project.hunsu.Repository;

import com.project.hunsu.Entity.Ootd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OotdRepository extends JpaRepository<Ootd,Long> {


    Ootd findByIdx(Long idx);

    void deleteByIdx(Long idx);

}
