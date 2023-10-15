package com.example.kdbe.service.impl;

import com.example.kdbe.model.entity.RefreshToken;
import com.example.kdbe.repository.RefreshTokenRepository;
import com.example.kdbe.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void saveRefToken(String userId, String refToken, String authToken) {
        refreshTokenRepository.save(new RefreshToken(userId, refToken, authToken));
    }

    @Override
    public void removeRefToken(String authToken) {
        refreshTokenRepository.findByAuthToken(authToken)
                .ifPresent(refreshTokenRepository::delete);
    }
}
