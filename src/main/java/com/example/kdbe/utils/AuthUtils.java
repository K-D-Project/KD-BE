package com.example.kdbe.utils;

import com.example.kdbe.auth.KdbeUserDetail;
import com.example.kdbe.model.entity.Credential;
import com.example.kdbe.model.entity.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthUtils {

    @Value("${auth.secretKey}")
    private String SECRET_KEY;

    @Value("${auth.AUTH_EXPIRY}")
    private long   AUTH_EXPIRY;

    @Value("${auth.REFRESH_EXPIRY}")
    private long   REFRESH_EXPIRY;

    private final PasswordEncoder passwordEncoder; // BCryptEncoder

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
        String subject = userDetail.getUsername() + ";";

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
     * @author 김세영
     * @since 2023-10-15
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

    /**
     * 서블릿 리퀘스트로 부터 토큰 추출
     * @author 김세영
     * @since 2023-10-15
     * @param req HttpServletRequest
     * @return 토큰 또는 Null
     */
    public String parseJwtFromHeader(HttpServletRequest req)
    {
        String token = req.getHeader("Authorization");
        if(StringUtils.hasText(token) && token.startsWith("Bearer "))
        {
            return token.substring(7);
        }

        return null;
    }


    /**
     * 인증 토큰으로부터 유저 ID 가져오기
     * @author 김세영
     * @since 2023-10-15
     * @param token 인증 토큰
     * @return 유저ID
     */
    public String getUserIdFromJwt(String token)
    {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token).getBody();

        String subject = claims.getSubject();

        String userId = subject.split(";")[0];

        if(userId == null) return null;

        return userId;
    }

    /**
     * 패스워드 인코딩(BCryptEncoder)
     * @param password
     * @return Encoded Password
     */
    public String encodePassword(String password)
    {
        return passwordEncoder.encode(password);
    }
}

