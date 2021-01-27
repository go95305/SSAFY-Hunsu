package com.project.hunsu.kakao.Repository;

import com.project.hunsu.Entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag,Long> {

}
