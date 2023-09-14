package com.lutzapi.presentation.controllers.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.application.services.TransactionService;
import com.lutzapi.domain.exceptions.handlers.ControllerExceptionHandler;
import com.lutzapi.presentation.controllers.UserController;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RestClientTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionControllerExceptionTest {
    @Mock
    private TransactionService transactionServiceMock;

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
    @DisplayName("EntityNotFoundException")
    public void itShouldReturn404WhenNotFound() throws Exception {
        TransactionDTO transaction = new TransactionDTO(null, 1L, 1L);

        when(transactionServiceMock.createTransaction(transaction))
                .thenThrow(EntityNotFoundException.class);

        ObjectMapper om = new ObjectMapper();
        mockMvc.perform(post("/users")
                        .content(om.writeValueAsString(transaction))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(transactionServiceMock, times(1)).createTransaction(transaction);
    }
}
