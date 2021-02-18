package com.project.hunsu.repository;

import com.project.hunsu.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findUserByNickname(String nickname);
    User findUserByJwtAccess(String jwtToken);
}
