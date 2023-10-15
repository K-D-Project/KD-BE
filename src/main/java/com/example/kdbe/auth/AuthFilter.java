package com.example.kdbe.auth;

import com.example.kdbe.utils.AuthUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@NoArgsConstructor
@Component
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private KdbeUserDetailService kdbeUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //TODO: JWT 토큰 검증 필터
        try
        {
            String authToken = authUtils.parseJwtFromHeader(request);

            if(authToken != null && authUtils.validateAuthToke(authToken))
            {
                String userId = authUtils.getUserIdFromJwt(authToken);

                KdbeUserDetail userDetail = kdbeUserDetailService.loadUserByUsername(userId);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetail, null, userDetail.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
        catch(IllegalArgumentException e)
        {
            log.error("Unalbe to get JWT Token: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        filterChain.doFilter(request, response);
    }
}
