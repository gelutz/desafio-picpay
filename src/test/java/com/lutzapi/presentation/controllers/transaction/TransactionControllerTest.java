package com.lutzapi.presentation.controllers.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.application.services.TransactionService;
import com.lutzapi.domain.entities.transaction.Transaction;
import com.lutzapi.domain.entities.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransactionControllerTest {
    @MockBean
    private TransactionService transactionService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("The endpoint should return an empty list when there are no users")
    void createTransaction() throws Exception {
        ObjectMapper om = new ObjectMapper();
        TransactionDTO transactionDTO = mock(TransactionDTO.class);
        Transaction transactionMock = mock(Transaction.class);

        when(transactionMock.getBuyer()).thenReturn(mock(User.class));
        when(transactionMock.getBuyer().getId()).thenReturn(1L);
        when(transactionService.createTransaction(transactionDTO)).thenReturn(transactionMock);

        String jsonTransaction = om.writeValueAsString(transactionDTO);
        this.mockMvc.perform(post("/transactions")
                        .content(jsonTransaction)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
        }
}