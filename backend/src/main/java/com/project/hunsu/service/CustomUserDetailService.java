package com.project.hunsu.service;

import com.project.hunsu.exception.ErrorCode;
import com.project.hunsu.exception.UserException;
import com.project.hunsu.model.entity.User;
import com.project.hunsu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return userRepository.findById(Long.valueOf(id)).orElseThrow(() -> new UserException(ErrorCode.UNEXPECTED_USER));
    }
}
