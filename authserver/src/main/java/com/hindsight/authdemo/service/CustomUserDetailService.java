package com.hindsight.authdemo.service;

import com.hindsight.authdemo.advice.exception.CUserNotFoundException;
import com.hindsight.authdemo.repository.UserJpaRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserJpaRepo userJpaRepo;

    @Override
    public UserDetails loadUserByUsername(String Uid){
//        return userJpaRepo.findById(Long.valueOf(userPk)).orElseThrow(CUserNotFoundException::new);
        return userJpaRepo.findByNickname(Uid);
    }
}
