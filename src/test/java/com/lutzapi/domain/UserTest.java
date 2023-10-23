package com.lutzapi.domain;


import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.exceptions.user.InsufficientFundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class UserTest {
    private User sut;

    @BeforeEach
    public void setUp() {
        sut = new User();
    }

    @Test
    @DisplayName("Throws InsufficientFundsException if the user doesnt have enough balance.")
    public void itShouldThrowIfUserDoesntHaveBalance() {
        BigDecimal userBalance = BigDecimal.valueOf(1);
        BigDecimal transactionAmount = userBalance.add(BigDecimal.valueOf(1));
        User buyer = mock(User.class);

        when(buyer.getBalance()).thenReturn(userBalance);

        assertThrows(InsufficientFundsException.class, () -> sut.subtractBalance(transactionAmount));
    }

    @Test
    @DisplayName("Throws RuntimeException if the amount to be added is negative.")
    public void itShouldThrowIfAmountIsNegative() {
        BigDecimal transactionAmount = BigDecimal.valueOf(-1);
        User buyer = mock(User.class);

        assertThrows(RuntimeException.class, () -> sut.addBalance(transactionAmount));
    }
}
