package com.example.kdbe.repository;

import com.example.kdbe.model.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByAuthToken(String authToken);
}
