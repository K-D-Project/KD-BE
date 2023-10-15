package com.example.kdbe.repository;

import com.example.kdbe.model.entity.User;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User user;
    User secondUser;

    @BeforeEach
    public void setup(){
        user = User.builder()
                .userId("test1234")
                .name("김세영")
                .email("ssss@naver.com")
                .build();

        secondUser = User.builder()
                .userId("admin1234")
                .name("김세영")
                .email("kim@naver.com")
                .build();
    }

    @Test
    @DisplayName("Repo test")
    public void testUser(){


        User saveUser = userRepository.save(user);
        System.out.println(saveUser);
        User findUser = userRepository.findById(saveUser.getId()).get();

        Assertions.assertThat(findUser.getId()).isEqualTo(user.getId());
        Assertions.assertThat(findUser).isEqualTo(user);
    }

    @Test
    @DisplayName("CRUD 검증")
    public void testCRUD(){

        userRepository.save(user);
        userRepository.save(secondUser);

        User findFirstUser = userRepository.findById(user.getId()).get();
        User findSecondUser = userRepository.findById(secondUser.getId()).get();

        Assertions.assertThat(findFirstUser).isEqualTo(user);
        Assertions.assertThat(findSecondUser).isEqualTo(secondUser);

        List<User> users = userRepository.findAll();
        Assertions.assertThat(users.size()).isEqualTo(2);

        long count = userRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        userRepository.delete(user);
        userRepository.delete(secondUser);

        long deleteCount = userRepository.count();
        Assertions.assertThat(deleteCount).isEqualTo(0);

    }

}