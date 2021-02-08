package com.project.hunsu.repository;

import com.project.hunsu.model.entity.Ootd;
import com.project.hunsu.model.entity.OotdLike;
import com.project.hunsu.model.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OotdRepository extends JpaRepository<Ootd,Long> {

    Ootd findByIdx(Long idx);
    Ootd findOotdByIdx(Long ootd_idx);
    List<Ootd> findOotdByUser(User user);
    List<Ootd> findBy(Pageable pageable);

}
