package com.project.hunsu.kakao.Repository;

import com.project.hunsu.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoRepository extends JpaRepository<User, String> {
}
