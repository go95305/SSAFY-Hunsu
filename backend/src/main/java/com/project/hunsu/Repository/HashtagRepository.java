package com.project.hunsu.Repository;

import com.project.hunsu.Entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag,Long> {

    List<Hashtag> findHashtagByOotdIdx(Long idx);

    List<Hashtag> findByContentContaining(String hashtag);
}
