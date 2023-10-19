package com.lutzapi.application.services;

import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.exceptions.repository.NotFoundException;
import com.lutzapi.domain.exceptions.user.InsufficientFundsException;
import com.lutzapi.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        sut = new UserService(userRepoMock, passwordEncoder);
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
    @DisplayName("Should update the given data and keep older data intact, then return the user.")
    public void itShouldReturnUpdatedUser() {
        UUID id = UUID.randomUUID();
        User mockedUserBeforeUpdate = mock(User.class);
        when(userRepoMock.findById(id)).thenReturn(Optional.of(mockedUserBeforeUpdate));

        String newName = "mock";
        User mockedUserAfterUpdate = User.builder()
                .id(id)
                .firstName(newName)
                .balance(BigDecimal.ONE)
                .build();

        when(userRepoMock.save(any(User.class))).thenReturn(mockedUserAfterUpdate);

        UserDTO dataToUpdate = UserDTO.builder().firstName(newName).build();
        User updatedUser = sut.updateUser(id, dataToUpdate);
        assertEquals(updatedUser.getFirstName(), newName);
        assertEquals(updatedUser.getBalance(), mockedUserAfterUpdate.getBalance());
    }

    @Test
    @DisplayName("Throws NotFoundException if the user is not found")
    public void itShouldThrowNotFound() {
        assertThrows(NotFoundException.class, () -> sut.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Throws InsufficientFundsException if the user doesnt have enough balance.")
    public void itShouldThrowIfUserDoesntHaveBalance(){
        BigDecimal userBalance = BigDecimal.valueOf(1);
        BigDecimal transactionAmount = userBalance.add(BigDecimal.valueOf(1));
        User buyer = mock(User.class);

        when(buyer.getBalance()).thenReturn(userBalance);

        assertThrows(InsufficientFundsException.class, () -> sut.subtractBalance(buyer, transactionAmount));
    }

    @Test
    @DisplayName("Throws RuntimeException if the amount to be added is negative.")
    public void itShouldThrowIfAmountIsNegative() {
        BigDecimal transactionAmount = BigDecimal.valueOf(-1);
        User buyer = mock(User.class);

        assertThrows(RuntimeException.class, () -> sut.addBalance(buyer, transactionAmount));
    }
}
