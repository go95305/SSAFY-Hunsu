package com.hindsight.authdemo.service.user;

import com.hindsight.authdemo.entity.User;
import com.hindsight.authdemo.repository.UserJpaRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {
    private  UserJpaRepo userJpaRepo;

    public void delete(String Uid) {
        Optional<User> user = userJpaRepo.findUserByUid(Uid);

        if(user.isPresent()){
            user.get().setFlag(false);
        }
    }

    public void update(User user){

    }

}
