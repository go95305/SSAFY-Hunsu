package com.hindsight.authdemo.repository;

import com.hindsight.authdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<User, Long> {

    Optional<User> findByNicknameAndProviderName(String nickname, String provider);
    Optional<User> findByOauthIdAndProviderName(String ouathId, String provider);
    User findByNickname(String nickname);
}
