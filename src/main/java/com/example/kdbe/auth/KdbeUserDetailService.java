package com.example.kdbe.auth;

import com.example.kdbe.repository.UserRepository;
import com.example.kdbe.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KdbeUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public KdbeUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found >> " + username));

        KdbeUserDetail kdbeUserDetail = new KdbeUserDetail();

        kdbeUserDetail.setUserId(user.getUserId());
        kdbeUserDetail.setEmail(user.getEmail());
        kdbeUserDetail.setName(user.getName());
        kdbeUserDetail.setCredential(user.getCredential());

        return kdbeUserDetail;
    }
}
