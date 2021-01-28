package com.hindsight.authdemo.repository;

import com.hindsight.authdemo.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserJpaRepoTest {

    @Autowired
    private UserJpaRepo userJpaRepo;

    @Test
    public void whenFindByUid_thenReturnUser(){
        String uid = "ssafy@ssafy.com";
        String name = "ssafy";

        //given
        userJpaRepo.save(
                User.builder()
                    .uid(uid)
                    .name(name)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build());

        //when
        Optional<User> user = userJpaRepo.findByUid(uid);
        //then
        assertNotNull(user);
        assertTrue(user.isPresent());
        assertEquals(user.get().getName(), name);
        assertThat(user.get().getName(), is(name));
    }

}