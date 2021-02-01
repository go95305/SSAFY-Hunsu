package com.project.hunsu.service;

import com.project.hunsu.model.entity.Follower;
import com.project.hunsu.model.entity.User;
import com.project.hunsu.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    @PersistenceContext
    EntityManager entityManager;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void followAdd(String myNickname, String yourNickname) {
        User member = entityManager.find(User.class, myNickname);
        User target = entityManager.find(User.class, yourNickname);
        Follower follower = new Follower();
        follower.setMemId(member);
        follower.setTargetId(target);
        entityManager.persist(follower);
    }


//    @Transactional
//    public void followDelete(String myNickname, String yourNickname) {
//        String query = " delete from Follower where followTo = : yourNickname";
//        int result = entityManager.createQuery(query).setParameter("yourNickname",yourNickname).executeUpdate();
//    }


}