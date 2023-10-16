package com.lutzapi.presentation.controllers;

import com.lutzapi.application.services.UserService;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserType;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {
    record AuthRequest(String username, String password) {
    }

    private UserService userService;
    private AuthenticationManager authenticationManager;

    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AuthRequest data) {
        UsernamePasswordAuthenticationToken up = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        authenticationManager.authenticate(up);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid AuthRequest data) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = User.builder()
                .firstName("123")
                .document("123")
                .username(data.username())
                .password(data.password())
                .email("123@123.com")
                .type(UserType.BUYER)
                .build();

        userService.createUser(newUser);

        return ResponseEntity.ok().build();
    }
}
