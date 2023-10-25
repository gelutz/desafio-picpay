package com.lutzapi.application.services;

import com.lutzapi.domain.entities.transaction.TransactionDTO;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserDTO;
import com.lutzapi.domain.entities.user.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SeedDatabaseService {
    final UserService userService;
    final TransactionService transactionService;

    Random random = new Random();

    List<User> users;

    public void seed(int rows) {
        seedUsers(rows);
        seedTransactions(rows);
    }

    public void seedTransactions(int rows) {
        List<TransactionDTO> transactions = new ArrayList<>();
        List<User> buyers = new ArrayList<>();
        List<User> sellers = new ArrayList<>();

        for (User user : this.users) {
            if (user.getType() == UserType.BUYER) {
                buyers.add(user);
            } else {
                sellers.add(user);
            }
        }

        int maxIndex = Math.min(rows, Math.min(buyers.size(), sellers.size()));

        for (int i = 0; i < rows; i++) {
            transactions.add(
                    new TransactionDTO(
                            buyers.get(random.nextInt(maxIndex)),
                            sellers.get(random.nextInt(maxIndex)),
                            new BigDecimal(randomFactor() / 1000)
                    )
            );
        }

        transactions.forEach(transactionService::saveTransaction);
    }

    public void seedUsers(int rows) {
        List<User> users = new ArrayList<>();
        users.add(createAdminUser());

        for (int i = 0; i < rows; i++) {
            users.add(createRandomUser());
        }

        this.users = users.stream().map(userService::saveUser).toList();
    }

    private User createAdminUser() {
        return User.builder()
                .firstName("Admin")
                .email("admin@email.com")
                .type(UserType.SELLER)
                .password("admin")
                .document(randomFactor() + "")
                .balance(new BigDecimal(randomFactor()))
                .build();
    }

    private User createRandomUser() {
        List<UserType> types = List.of(UserType.SELLER, UserType.BUYER);
        UserDTO dto = new UserDTO(
                "abc" + randomFactor(),
                "cba" + randomFactor(),
                "mock" + randomFactor() + "@example.com",
                "" + randomFactor(),
                types.get(randomFactor() % 2),
                new BigDecimal(randomFactor()).multiply(new BigDecimal(1000))
        );

        return User.fromDTO(dto);
    }

    private int randomFactor() {
        int randomNumber = 13711735;
        return random.nextInt(randomNumber);
    }
}
