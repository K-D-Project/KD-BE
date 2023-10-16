package com.example.kdbe.service.impl;

import com.example.kdbe.auth.KdbeUserDetail;
import com.example.kdbe.auth.KdbeUserDetailService;
import com.example.kdbe.exception.RoleNotFoundException;
import com.example.kdbe.model.dto.request.SignInRequestDto;
import com.example.kdbe.model.dto.request.SignUpRequestDto;
import com.example.kdbe.model.dto.response.SignInResponseDto;
import com.example.kdbe.model.entity.RefreshToken;
import com.example.kdbe.model.entity.Role;
import com.example.kdbe.model.mapStruct.StructMapper;
import com.example.kdbe.model.mapStruct.UserMapper;
import com.example.kdbe.model.dto.request.UserRequestDto;
import com.example.kdbe.model.entity.User;
import com.example.kdbe.repository.BaseRepository;
import com.example.kdbe.repository.RefreshTokenRepository;
import com.example.kdbe.repository.RoleRepository;
import com.example.kdbe.repository.UserRepository;
import com.example.kdbe.service.UserService;
import com.example.kdbe.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository         userRepository;
    private final UserMapper             userMapper;
    private final AuthUtils              authUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RoleRepository         roleRepository;

    @Override
    public BaseRepository<User> getRepository() {
        return userRepository;
    }

    @Override
    public StructMapper<User,UserRequestDto> getMapper() {
        return userMapper;
    }

    /**
     * 로그인
     * @author 김세영
     * @since 2023-10-15
     * @param req SignInRequestDto
     * @param userDetail 컨트롤러에서 던져주는 UserDetail
     * @return SignInResponseDto (AuthToken, RefToken)
     */
    @Override
    public SignInResponseDto signIn(SignInRequestDto req, KdbeUserDetail userDetail) {
        SignInResponseDto res = new SignInResponseDto();

        //TODO: Set Authentication to SecurityContextHolder & Generate AuthToken, RefToken
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetail, req.getPassword(), userDetail.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authToken = authUtils.generateAuthToken(authentication);
        String refToken = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(userDetail.getUsername(), refToken, authToken);
        refreshTokenRepository.save(refreshToken);

        //TODO: Set SignInResponse
        res.setAuthToken(authToken);
        res.setRefToken(refToken);

        return res;
    }

    /**
     * 회원가입
     * @author 김세영
     * @since 2023-10-15
     * @param req SignUpRequestDto
     * @return true or false
     */
    @Override
    public boolean signUp(SignUpRequestDto req) {

        //Validation
        if(userRepository.existsByUserId(req.getUserId())) return false;
        if(userRepository.existsByEmail(req.getEmail())) return false;

        Role r = null;
        try {
            r = roleRepository.findRoleByRoleId(req.getRole())
                    .orElseThrow(() -> new RoleNotFoundException("Role Not Found"));
        } catch (RoleNotFoundException e) {
            return false;
        }

        User newUser = User.builder()
                .userId(req.getUserId())
                .password(authUtils.encodePassword(req.getPassword()))
                .email(req.getEmail())
                .role(r)
                .build();

        userRepository.save(newUser);

        return true;
    }

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
