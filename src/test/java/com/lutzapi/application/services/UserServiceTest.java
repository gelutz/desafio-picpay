package com.lutzapi.application.services;

import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.domain.exceptions.user.MissingInfoException;
import com.lutzapi.infrastructure.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Test
    public void itThrowsWhenMissingData() {
        UserDTO user = Mockito.mock(UserDTO.class); // all fields are null
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        UserService us = new UserService(userRepository);

        Assertions.assertThrows(MissingInfoException.class, () -> us.createUser(user));
    }
}
