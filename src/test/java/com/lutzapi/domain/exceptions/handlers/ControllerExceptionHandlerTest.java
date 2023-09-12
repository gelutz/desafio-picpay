package com.lutzapi.domain.exceptions.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.application.services.UserService;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserType;
import com.lutzapi.domain.exceptions.LutzExceptionResponse;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import com.lutzapi.presentation.controllers.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ControllerExceptionHandlerTest {
    @Mock
    private UserService userServiceMock; // é usado dentro do userController
    @InjectMocks
    private UserController userController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(ControllerExceptionHandler.class)
                .build();
    }

    @Test
    @DisplayName("MissingDataException")
    public void createUserShouldThrowIfThereIsMissingData() throws Exception {
        // por algum motivo precisa ser assim, usando Mockito.mock(UserDTO.class) o retorno do controller é 201 (wtf?)
        UserDTO user = new UserDTO(null, null, null, null, null, null);

        List<String> emptyFields = new ArrayList<>();
        emptyFields.add("First Name");
        emptyFields.add("Document");
        emptyFields.add("Type");

        when(userServiceMock.createUser(user)).thenThrow(new MissingDataException(emptyFields));

        ObjectMapper om = new ObjectMapper();
        mockMvc.perform(post("/users")
                        .content(om.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(userServiceMock, times(1)).createUser(user);
    }

    @Test
    public void itShouldNotCreateTwoUsersWithSameInfo() throws Exception {
//        String email = "email@email.com";
//        String document = "mockedDocument";
//        UserDTO user = new UserDTO(null, null, email, document, UserType.BUYER, BigDecimal.ONE);
        UserDTO user = new UserDTO(null, null, null, null, null, null);

        when(userServiceMock.createUser(user))
                .thenThrow(DataIntegrityViolationException.class);

        ObjectMapper om = new ObjectMapper();
        mockMvc.perform(post("/users")
                        .content(om.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());


        verify(userServiceMock, times(1)).createUser(user);
    }
}
