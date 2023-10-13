package com.example.kdbe.utils;

import com.example.kdbe.auth.KdbeUserDetail;
import io.jsonwebtoken.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@Component
@Slf4j
public class AuthUtils {

    @Value("${auth.secretKey}")
    private String SECRET_KEY;

    @Value("${auth.AUTH_EXPIRY}")
    private long   AUTH_EXPIRY;

    @Value("${auth.REFRESH_EXPIRY}")
    private long   REFRESH_EXPIRY;

    /**
     * 인증 토큰 생성 (시큐리티 인증 객체로부터)
     * @param authentication 스프링 시큐리티 인증 객체
     * @author 김세영
     * @since 2023-10-11
     * @return 인증 토큰(Subject = UserId + ?)
     */
    public String generateAuthToken(Authentication authentication) {
        KdbeUserDetail userDetail = (KdbeUserDetail) authentication.getPrincipal();

        // Setting Subject
        String subject = userDetail.getUsername();

        log.info("AuthToken Generated >> Subject: {}", subject);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + AUTH_EXPIRY))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }


    /**
     * Jwt 유효성 검증
     * @param jwt 토큰
     * @return True Or False
     */
    public boolean validateAuthToke(String jwt)
    {
        try
        {
            log.info("Token Validating: {}", jwt);

            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt).getBody();

            return true;
        }
        catch(ExpiredJwtException e)
        {
            log.error("AuthToken Expired: {}", e);
            return false;
        }
        catch(JwtException e)
        {
            log.error("Illegal AuthToken: {}", e);
            return false;
        }
    }
}

