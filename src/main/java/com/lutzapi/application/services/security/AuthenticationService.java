package com.lutzapi.application.services.security;


import com.lutzapi.application.dtos.UserLoginDTO;
import com.lutzapi.application.dtos.UserRegisterDTO;
import com.lutzapi.application.services.UserService;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.infrastructure.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public User signup(UserRegisterDTO input) {
        User user = User.builder()
                .firstName(input.name())
                .email(input.email())
                .password(input.password())
                .build();

        return userService.createUser(user);
    }

    public User authenticate(UserLoginDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.email(),
                        input.password()
                )
        );

        return userService.findByEmail(input.email());
    }
}
