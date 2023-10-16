package com.lutzapi.application.services;

import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.exceptions.repository.NotFoundException;
import com.lutzapi.domain.exceptions.user.InsufficientFundsException;
import com.lutzapi.infrastructure.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("username", username));
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Buyer ID", id + ""));
    }

    public User createUser(User user) {
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

    public void subtractBalance(User user, BigDecimal amount) {
        if (user.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(user.getId());
        }

        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);
    }

    public void addBalance(User user, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor não pode ser negativo/zero.");
        }

        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }
}
