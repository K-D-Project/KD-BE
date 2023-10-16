package com.example.kdbe.model.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private PasswordEncoder passwordEncoder;

    User user;
    User secondUser;

    @BeforeEach
    public void setUp(){
        user = User.builder()
                .userId("test1234")
                .name("kim")
                .email("ssss@naver.com")
                .password("testtest")
                .build();

        secondUser = User.builder()
                .userId("test2")
                .build();
    }

    @Test
    @Transactional
    @DisplayName("영속성 테스트")
    public void testPersist(){
        em.persist(user);
        em.persist(secondUser);
        List<User> users = em.createQuery("select u from User u", User.class).getResultList();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @DisplayName("엔티티 검증")
    public void testEntity(){
        em.persist(user);
        User result = em.createQuery("select u from User u where u.userId = 'test1234'", User.class).getSingleResult();
        Assertions.assertThat(user).isEqualTo(result);
    }

    @Test
    @Transactional
    @DisplayName("Credential 검증")
    public void testCredential(){

        em.persist(user);

        User addCredential = em.createQuery("select u from User u where u.userId = 'test1234'", User.class).getSingleResult();
        Assertions.assertThat(passwordEncoder.encode(user.getPassword())).isEqualTo(addCredential.getPassword());
    }

    @Test
    @Transactional
    @DisplayName("Role 검증")
    public void testRole(){
        Role role = Role.builder()
                .roleId("uuid123")
                .type("admin")
                .build();

        em.persist(user);
        em.persist(role);

        user.changeRole(role);

        User addRole = em.createQuery("select u from User u where u.userId = 'test1234'", User.class).getSingleResult();
        Assertions.assertThat(role).isEqualTo(addRole.getRole());
        System.out.println(addRole.getRole().getRoleId());

    }
}