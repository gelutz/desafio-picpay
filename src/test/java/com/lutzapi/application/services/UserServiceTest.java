package com.lutzapi.application.services;

import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.exceptions.BadDataException;
import com.lutzapi.domain.exceptions.user.InsufficientFundsException;
import com.lutzapi.domain.exceptions.user.MissingDataException;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
        when(mockedUser1.getId()).thenReturn(UUID.randomUUID());
        when(mockedUser2.getId()).thenReturn(UUID.randomUUID());

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
        when(mockedUser1.getId()).thenReturn(UUID.randomUUID());

        when(userRepoMock.findById(any(UUID.class))).thenReturn(Optional.of(mockedUser1));

        User returnedUser = sut.findById(UUID.randomUUID());
        assertEquals(returnedUser.getId(), mockedUser1.getId());
    }


    @Test
    @DisplayName("Should return the user that was just created")
    public void itShouldReturnCreatedUser() {
        String email = "email@mock.com";
        User userMock = mock(User.class);

        when(userMock.getEmail()).thenReturn(email);
        when(userRepoMock.save(any(User.class))).thenReturn(userMock);

        User response = sut.createUser(userMock);

        assertInstanceOf(User.class, response);
        assertEquals(response.getEmail(), email);
    }

    @Test
    @DisplayName("Throws MissingDataException when there are missing fields (buyerID, sellerID or amount)")
    public void itThrowsWhenMissingData() {
        UserDTO user = mock(UserDTO.class); // all fields are null
        assertThrows(MissingDataException.class, () -> sut.validateUserData(user));
    }

    @Test
    @DisplayName("Throws if the user doesnt have enough balance.")
    public void itShouldThrowIfUserDoesntHaveBalance(){
        BigDecimal userBalance = BigDecimal.valueOf(1);
        BigDecimal transactionAmount = userBalance.add(BigDecimal.valueOf(1));
        User buyer = mock(User.class);

        when(buyer.getBalance()).thenReturn(userBalance);

        assertThrows(InsufficientFundsException.class, () -> sut.subtractBalance(buyer, transactionAmount));
    }

    @Test
    @DisplayName("Throws if the amount to be added is negative.")
    public void itShouldThrowIfAmountIsNegative() {
        BigDecimal transactionAmount = BigDecimal.valueOf(-1);
        User buyer = mock(User.class);

        BadDataException exception = assertThrows(BadDataException.class,
                () -> sut.addBalance(buyer, transactionAmount));

        assertTrue(exception.getMessage().contains("negativo"));
    }
}
