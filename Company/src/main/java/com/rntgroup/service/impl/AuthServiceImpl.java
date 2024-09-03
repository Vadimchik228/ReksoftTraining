package com.rntgroup.service.impl;

import com.rntgroup.database.entity.User;
import com.rntgroup.database.repository.UserRepository;
import com.rntgroup.service.AuthService;
import com.rntgroup.web.dto.auth.JwtRequest;
import com.rntgroup.web.dto.auth.JwtResponse;
import com.rntgroup.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtResponse login(final JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword())
        );

        Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());

        if (user.isPresent()) {
            jwtResponse.setId(user.get().getId());
            jwtResponse.setUsername(user.get().getUsername());
            jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(
                    user.get().getId(), user.get().getUsername(), user.get().getRoles())
            );

            jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(
                    user.get().getId(), user.get().getUsername())
            );

            return jwtResponse;
        }

        return null;
    }

    @Override
    public JwtResponse refresh(final String refreshToken) {
        return jwtTokenProvider.refreshUserTokens(refreshToken);
    }
}
