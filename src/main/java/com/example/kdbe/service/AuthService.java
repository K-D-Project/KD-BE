package com.example.kdbe.service;

public interface AuthService {
    public void saveRefToken(String userId, String refToken, String authToken);
    public void removeRefToken(String authToken);
}
