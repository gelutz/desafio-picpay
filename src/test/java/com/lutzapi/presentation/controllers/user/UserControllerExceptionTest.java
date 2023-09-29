package com.lutzapi.presentation.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.application.services.UserService;
import com.lutzapi.domain.exceptions.handlers.ControllerExceptionHandler;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import com.lutzapi.presentation.controllers.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// depois de alguns testes descobri que essa classe não testa o @ControllerAdvice
@RestClientTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerExceptionTest {
    @Mock
    private UserService userServiceMock;

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
        emptyFields.add("Email");

        doThrow(new MissingDataException(emptyFields)).when(userServiceMock).validateUserData(user);

        ObjectMapper om = new ObjectMapper();
        mockMvc.perform(post("/users")
                        .content(om.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

        verify(userServiceMock, times(1)).validateUserData(user);
    }

    @Test
    @DisplayName("DataIntegrityViolationException")
    public void itShouldNotCreateTwoUsersWithSameInfo() throws Exception {
        UserDTO user = new UserDTO(null, null, null, null, null, null);

        when(userServiceMock.createUser(user))
                .thenThrow(DataIntegrityViolationException.class);

        ObjectMapper om = new ObjectMapper();
        mockMvc.perform(post("/users")
                        .content(om.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());

        verify(userServiceMock, times(1)).createUser(user);
    }
}
