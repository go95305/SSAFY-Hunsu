package com.hindsight.authdemo.repository;

import com.hindsight.authdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<User, Long> {


//    Optional<User> findByNicknameAndProviderName(String nickname, String provider);
//    Optional<User> findByOauthIdAndProviderName(String ouathId, String provider);
//    User findByNickname(String nickname);

    Optional<User> findByUid(String UID);

    UserDetails findByNickname(String nickname);
}
