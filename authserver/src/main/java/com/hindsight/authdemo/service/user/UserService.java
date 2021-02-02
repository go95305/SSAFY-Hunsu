package com.hindsight.authdemo.service.user;

import com.hindsight.authdemo.entity.User;
import com.hindsight.authdemo.repository.UserJpaRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {
    private  UserJpaRepo userJpaRepo;

    public void delete(String Uid) {
        User user = userJpaRepo.findUserByUid(Uid);

        user.setFlag(false);
        userJpaRepo.save(user);
    }

    public void updateAccessToken(String Uid, String token){
        User user = userJpaRepo.findUserByUid(Uid);

        user.setAccessToken(token);
        userJpaRepo.save(user);
    }

    public  void joinUser(String uid, String accessToken, String refreshToken, String gender,boolean flag){
        User user = new User();

        user.setUid(uid);
        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);
        user.setGender(gender);
        user.setFlag(flag);

        User saveuser = userJpaRepo.save(user);

    }
    public void addUserInfo(String Uid, String nickname,double height,String size,String jwtRefresh, String jwtAccess,boolean flag){
        User user = userJpaRepo.findUserByUid(Uid);

        user.setNickname(nickname);
        user.setHeight(height);
        user.setSize(size);
        user.setJwtRefresh(jwtRefresh);
        user.setJwtAccess(jwtAccess);
        user.setFlag(flag);

        userJpaRepo.save(user);
    }


}
