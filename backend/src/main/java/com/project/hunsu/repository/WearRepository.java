package com.project.hunsu.repository;

import com.project.hunsu.model.entity.User;
import com.project.hunsu.model.entity.Wear;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WearRepository extends JpaRepository<Wear,Long> {

    List<Wear> findByFlag(Boolean flag, Pageable pageable);
    Long countByFlag(Boolean flag);
    Wear findWearByIdx(Long idx);
    List<Wear> findWearByUser(User user);

}
