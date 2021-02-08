package com.hindsight.authdemo.service.user;

import com.hindsight.authdemo.entity.User;
import com.hindsight.authdemo.repository.UserJpaRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {
    private  UserJpaRepo userJpaRepo;
    public UserService(UserJpaRepo userJpaRepo) {
        this.userJpaRepo = userJpaRepo;
    }

    public void delete(long Uid) {
        User user = userJpaRepo.findUserByUid(Uid);
        user.setFlag(false);
        userJpaRepo.save(user);
    }

    public void updateAccessToken(long Uid, String token){
        User user = userJpaRepo.findUserByUid(Uid);
        user.setAccessToken(token);
        userJpaRepo.save(user);
    }

    public void updatejwtToken(long Uid, String token){
        User user = userJpaRepo.findUserByUid(Uid);
        user.setJwtAccess(token);
        userJpaRepo.save(user);
    }


    public  void joinUser(long uid, String accessToken, String refreshToken,boolean flag){
        User user = new User();

        user.setUid(uid);
        System.out.println("set uid");
        user.setAccessToken(accessToken);
        System.out.println("set aT");
        user.setRefreshToken(refreshToken);
        System.out.println("set rT");
        user.setHeight(0);
        System.out.println("set He");
//        user.setGender(gender);
        user.setFlag(flag);
        System.out.println("set flag");
        userJpaRepo.save(user);
        System.out.println("save user");

    }

    public void setAllTokens(long Uid, String accessToken, String refreshToken, String jwtToken, String jwtRefresh){
        User user=userJpaRepo.findUserByUid(Uid);

        user.setAccessToken(accessToken);
        user.setRefreshToken(refreshToken);
        user.setJwtAccess(jwtToken);
        user.setJwtRefresh(jwtRefresh);

        userJpaRepo.save(user);
    }



    public void addUserInfo(long Uid, String nickname,double height,String size,String jwtRefresh, String jwtAccess,boolean flag){
        User user = userJpaRepo.findUserByUidAndFlag(Uid,false);


        user.setNickname(nickname);
        user.setHeight(height);
        user.setSize(size);
        user.setJwtRefresh(jwtRefresh);
        user.setJwtAccess(jwtAccess);
        user.setFlag(flag);

        userJpaRepo.save(user);
    }


}
