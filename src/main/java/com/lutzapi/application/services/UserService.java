package com.lutzapi.application.services;

import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserDTO;
import com.lutzapi.domain.exceptions.repository.NotFoundException;
import com.lutzapi.infrastructure.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;


    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Buyer ID", id + ""));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Buyer ID", email));
    }

    public User saveUser(User user) {
        if (user.getPassword() != null)
            user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User updateUser(UUID id, UserDTO userData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Buyer ID", id + ""));

        if (userData.email() != null)
            user.setEmail(userData.email());
        if (userData.firstName() != null)
            user.setFirstName(userData.firstName());
        if (userData.lastName() != null)
            user.setLastName(userData.lastName());
        if (userData.document() != null)
            user.setDocument(userData.document());
        if (userData.balance() != null)
            user.setBalance(userData.balance());
        if (userData.type() != null)
            user.setType(userData.type());

        return userRepository.save(user);
    }
}
