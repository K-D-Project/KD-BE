package com.example.kdbe.model.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@Getter
@RedisHash(value="refToken", timeToLive=60*60*24*3)
public class RefreshToken {

    @Id
    private String id;

    private String refreshToken;

    @Indexed
    private String authToken; //Indexed 어노테이션이 달린 필드로만 검색 가능.

}
