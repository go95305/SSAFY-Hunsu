package com.hindsight.authdemo.repository;

import com.hindsight.authdemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserJpaRepo extends JpaRepository<User, Long> {


//    Optional<User> findByNicknameAndProviderName(String nickname, String provider);
//    Optional<User> findByOauthIdAndProviderName(String ouathId, String provider);
//    User findByNickname(String nickname);


    Optional<User> findUserByJwtRefresh(String jwtRefreshToken);

    Optional<User> findUserByUidAndFlag(String Uid, Boolean flag);       // User테이블에서 Uid

//    Optional<User> findUserByNickname(String nickname);

    User findUserByUid(String Uid);


}
