package com.lutzapi.application.services;

import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserType;
import com.lutzapi.domain.exceptions.user.InsufficientFundsException;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import com.lutzapi.domain.exceptions.user.WrongUserTypeException;
import com.lutzapi.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    private UserService sut;
    @Mock
    private UserRepository userRepoMock;

    @BeforeEach
    public void setUp() {
        sut = new UserService(userRepoMock);
    }

    @Test
    @DisplayName("Should return a list of Users")
    public void itShouldReturnAListOfUsers() {
        User mockedUser1 = mock(User.class);
        User mockedUser2 = mock(User.class);
        when(mockedUser1.getId()).thenReturn(1L);
        when(mockedUser2.getId()).thenReturn(2L);

        List<User> users = new ArrayList<>();
        users.add(mockedUser1);
        users.add(mockedUser2);

        when(userRepoMock.findAll()).thenReturn(users);

        Iterable<User> returnedUsers = sut.getAllUsers();
        assertEquals(returnedUsers.iterator().next().getId(), users.get(0).getId());
    }

    @Test
    @DisplayName("Should return user with given id")
    public void itShouldReturnUserWithGivenId() {
        User mockedUser1 = mock(User.class);
        when(mockedUser1.getId()).thenReturn(1L);

        when(userRepoMock.findById(anyLong())).thenReturn(Optional.of(mockedUser1));

        User returnedUser = sut.findById(1L);
        assertEquals(returnedUser.getId(), mockedUser1.getId());
    }

    @Test
    @DisplayName("Should return nothing if user is validated")
    public void itShouldReturnNothingIfUserIsValidated() {
        User mockedUser = mock(User.class);
        when(mockedUser.getType()).thenReturn(UserType.BUYER);
        when(mockedUser.getBalance()).thenReturn(BigDecimal.valueOf(10));

        assertDoesNotThrow(() -> sut.validateUserForTransaction(mockedUser, BigDecimal.valueOf(9)));
    }

    @Test
    @DisplayName("Should throw MissingDataException when there are missing fields (buyerID, sellerID or amount)")
    public void itThrowsWhenMissingData() {
        UserDTO user = mock(UserDTO.class); // all fields are null
        assertThrows(MissingDataException.class, () -> sut.createUser(user));
    }

    @Test
    @DisplayName("Should throw WrongUserTypeException if the user's type is SELLER.")
    public void itShouldthrowIfUserIsSeller(){
        BigDecimal transactionAmount = BigDecimal.valueOf(1);
        User buyer = mock(User.class);
        when(buyer.getType()).thenReturn(UserType.SELLER);

        assertThrows(WrongUserTypeException.class, () -> sut.validateUserForTransaction(buyer, transactionAmount));
    }

    @Test
    @DisplayName("Should throw if the user doesnt have enough balance.")
    public void itShouldThrowIfUserDoesntHaveBalance(){
        BigDecimal userBalance = BigDecimal.valueOf(1);
        BigDecimal transactionAmount = userBalance.add(BigDecimal.valueOf(1));
        User buyer = mock(User.class);

        when(buyer.getType()).thenReturn(UserType.BUYER);
        when(buyer.getBalance()).thenReturn(userBalance);

        assertThrows(InsufficientFundsException.class, () -> sut.validateUserForTransaction(buyer, transactionAmount));
    }
}
