package com.lutzapi.presentation.controllers;


import com.lutzapi.application.dtos.UserLoginDTO;
import com.lutzapi.application.dtos.UserRegisterDTO;
import com.lutzapi.application.services.security.AuthenticationService;
import com.lutzapi.application.services.security.JwtService;
import com.lutzapi.domain.entities.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public User register(@Valid @RequestBody UserRegisterDTO registerUserDto) {
        return authenticationService.signup(registerUserDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Map<String, String> authenticate(@Valid @RequestBody UserLoginDTO loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("expires", jwtService.getJwtExpiration() + "");

        return response;
    }
}
