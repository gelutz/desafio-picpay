package com.lutzapi.domain.exceptions.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.application.services.UserService;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import com.lutzapi.presentation.controllers.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
    public void createUserException() throws Exception {
        // por algum motivo precisa ser assim, usando Mockito.mock(UserDTO.class) o retorno do controller é 201 (wtf?)
        UserDTO user = new UserDTO(null, null, null, null, null, null);

        List<String> emptyFields = new ArrayList<>();
        emptyFields.add("Buyer ID");
        emptyFields.add("Seller ID");
        emptyFields.add("Amount");

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
    public void createTransactionException() throws Exception {
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
