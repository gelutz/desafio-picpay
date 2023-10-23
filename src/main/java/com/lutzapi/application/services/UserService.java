package com.lutzapi.application.services;

import com.lutzapi.application.dtos.CreateTransactionDTO;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserDTO;
import com.lutzapi.domain.entities.user.UserType;
import com.lutzapi.domain.exceptions.repository.NotFoundException;
import com.lutzapi.domain.exceptions.user.InsufficientFundsException;
import com.lutzapi.infrastructure.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TransactionService transactionService;

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

    public void seed(int rows) {

        List<User> buyers = new ArrayList<>();
        List<User> sellers = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            User user = saveUser(createRandomUser());

            if (user.getType() == UserType.SELLER) sellers.add(user);
            if (user.getType() == UserType.BUYER) buyers.add(user);
        }

        for (int i = 0; i < rows; i++) {
            int randomFactor = (int) (Math.random() * rows);
            CreateTransactionDTO transactionDTO = new CreateTransactionDTO(
                    BigDecimal.valueOf(Math.random() * 10),
                    sellers.get(randomFactor).getId(),
                    buyers.get(randomFactor).getId()
            );

            transactionService.saveTransaction(transactionDTO);
        }

    }

    private User createRandomUser() {
        List<UserType> types = List.of(UserType.SELLER, UserType.BUYER);

        int randomFactor = (int) ((Math.random() * 100) * (Math.random() * 100));

        return User.builder()
                .firstName("abc" + randomFactor)
                .lastName("cba" + randomFactor)
                .email("mock" + randomFactor + "@example.com")
                .type(types.get(randomFactor % 2))
                .document("" + randomFactor)
                .password((randomFactor * 2) + "")
                .balance(BigDecimal.valueOf(Math.random() * 1000))
                .build();

    }
}
