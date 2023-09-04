package com.lutzapi.application.services;

import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserType;
import com.lutzapi.domain.exceptions.user.InsufficientFundsException;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import com.lutzapi.domain.exceptions.user.WrongUserTypeException;
import com.lutzapi.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;


@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    private UserService sut;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        sut = new UserService(userRepository);
    }

    @Test
    public void itThrowsWhenMissingData() {
        UserDTO user = Mockito.mock(UserDTO.class); // all fields are null
        Assertions.assertThrows(MissingDataException.class, () -> sut.createUser(user));
    }

    @Test
    @DisplayName("Should throw WrongUserTypeException if the user's type is SELLER.")
    public void itShouldthrowIfUserIsSeller(){
        BigDecimal transactionAmount = BigDecimal.valueOf(1);
        User buyer = Mockito.mock(User.class);
        Mockito.when(buyer.getType()).thenReturn(UserType.SELLER);

        Assertions.assertThrows(WrongUserTypeException.class, () -> sut.validateUserForTransaction(buyer, transactionAmount));
    }

    @Test
    @DisplayName("Should throw if the user doesnt have enough balance.")
    public void itShouldThrowIfUserDoesntHaveBalance(){
        BigDecimal userBalance = BigDecimal.valueOf(1);
        BigDecimal transactionAmount = userBalance.add(BigDecimal.valueOf(1));
        User buyer = Mockito.mock(User.class);

        Mockito.when(buyer.getType()).thenReturn(UserType.BUYER);
        Mockito.when(buyer.getBalance()).thenReturn(userBalance);

        Assertions.assertThrows(InsufficientFundsException.class, () -> sut.validateUserForTransaction(buyer, transactionAmount));
    }
}
