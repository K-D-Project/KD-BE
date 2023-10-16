package com.example.kdbe.service;

import com.example.kdbe.auth.KdbeUserDetail;
import com.example.kdbe.auth.KdbeUserDetailService;
import com.example.kdbe.model.dto.request.SignInRequestDto;
import com.example.kdbe.model.dto.request.SignUpRequestDto;
import com.example.kdbe.model.dto.response.SignInResponseDto;
import com.example.kdbe.service.impl.UserServiceImpl;
import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    KdbeUserDetailService userDetailService;

    SignUpRequestDto signUpRequestDto = new SignUpRequestDto();

    SignInRequestDto signInRequestDto = new SignInRequestDto();

    @BeforeEach
    void before()
    {
        signUpRequestDto.setUserId("ksy2008w");
        signUpRequestDto.setEmail("ksy2008w@naver.com");
        signUpRequestDto.setPassword("rlatpdud13795@");
        signUpRequestDto.setRole("ROLE_USER");

        signInRequestDto.setUserId("ksy2008w");
        signInRequestDto.setPassword("rlatpdud13795@");
    }

    @Test
    void signUp()
    {
        Assertions.assertTrue(userService.signUp(signUpRequestDto));
    }

    @Test
    void signIn()
    {
        KdbeUserDetail userDetail = userDetailService.loadUserByUsername("ksy2008w");

        SignInResponseDto response = userService.signIn(signInRequestDto, userDetail);

        System.out.println(response.getAuthToken());
        System.out.println(response.getRefToken());
    }


}
