package com.project.hunsu.Repository;

import com.project.hunsu.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    User findUserByNickname(String nickname);
}
