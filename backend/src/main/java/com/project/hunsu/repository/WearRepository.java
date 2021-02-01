package com.project.hunsu.repository;

import com.project.hunsu.model.entity.Wear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WearRepository extends JpaRepository<Wear,Long> {

    List<Wear> findWearByFlagOrderByWriteDate(Boolean flag);
    Wear findWearByIdx(Long idx);

}
