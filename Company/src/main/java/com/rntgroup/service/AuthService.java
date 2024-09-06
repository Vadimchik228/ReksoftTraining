package com.rntgroup.service;

import com.rntgroup.web.dto.auth.JwtRequest;
import com.rntgroup.web.dto.auth.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest loginRequest);

    JwtResponse refresh(String refreshToken);
}
