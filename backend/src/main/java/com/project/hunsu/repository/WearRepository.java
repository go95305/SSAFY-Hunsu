package com.project.hunsu.repository;

import com.project.hunsu.model.entity.Wear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@Service
//@Transactional
public interface WearRepository extends JpaRepository<Wear,Long> {

    List<Wear> findWearByOrderByWriteDate();
    void deleteWearByIdx(Long idx);
    Wear findWearByIdx(Long idx);

}
