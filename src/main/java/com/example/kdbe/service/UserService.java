package com.example.kdbe.service;

import com.example.kdbe.model.dto.request.SignInRequestDto;
import com.example.kdbe.model.dto.request.UserRequestDto;
import com.example.kdbe.model.dto.response.SignInResponseDto;
import com.example.kdbe.model.entity.User;
import jakarta.transaction.Transactional;

@Transactional
public interface UserService extends BaseService<User,UserRequestDto> {

    public SignInResponseDto signIn(SignInRequestDto req);
    public void saveRefToken(String userId, String refToken, String authToken);
    public void removeRefToken(String authToken);
}
