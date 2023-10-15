package com.lutzapi.application.services;

import com.lutzapi.infrastructure.repositories.UserRepository;
import com.lutzapi.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    final AuthenticationManager authenticationManager;
    final UserRepository userRepository;
    final PasswordEncoder encoder;
    final JwtUtils jwtUtils;

    public ResponseCookie getCleanCookie() {
        return jwtUtils.getCleanJwtCookie();
    }
}
