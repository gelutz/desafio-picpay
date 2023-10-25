package com.lutzapi.application.services;

import com.lutzapi.domain.entities.transaction.TransactionDTO;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserDTO;
import com.lutzapi.domain.entities.user.UserType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class SeedDatabaseService {
    UserService userService;
    TransactionService transactionService;
    final Random random = new Random();
    final int randomNumber = 13711735;

    public void seedTransactions(List<User> users) {
        List<TransactionDTO> transactions = new ArrayList<>();
        List<User> buyers = new ArrayList<>();
        List<User> sellers = new ArrayList<>();

        for (User user : users) {
            if (user.getType() == UserType.BUYER) {
                buyers.add(user);
            } else {
                sellers.add(user);
            }
        }

        int size = Math.min(buyers.size(), sellers.size());
        for (int i = 0; i < size; i++) {
            transactions.add(
                    createRandomTransactionDTO(
                            buyers.get(i),
                            sellers.get(i),
                            new BigDecimal(randomFactor())
                    )
            );
        }

        transactions.forEach(transactionService::saveTransaction);
    }

    private TransactionDTO createRandomTransactionDTO(User buyer, User seller, BigDecimal amount) {
        return new TransactionDTO(buyer, seller, amount);
    }

    public List<User> seedUsers(int rows) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            users.add(User.fromDTO(createRandomUserDTO()));
        }

        return users
                .stream()
                .map(userService::saveUser)
                .toList();
    }

    private UserDTO createRandomUserDTO() {
        List<UserType> types = List.of(UserType.SELLER, UserType.BUYER);

        return new UserDTO(
                "abc" + randomFactor(),
                "cba" + randomFactor(),
                "mock" + randomFactor() + "@example.com",
                "" + randomFactor(),
                types.get(randomFactor() % 2),
                new BigDecimal(randomFactor()).multiply(new BigDecimal(1000))
        );
    }

    private int randomFactor() {
        return random.nextInt(randomNumber);
    }
}
