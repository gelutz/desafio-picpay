package com.lutzapi.presentation.controllers.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lutzapi.application.dtos.CreateTransactionDTO;
import com.lutzapi.application.services.TransactionService;
import com.lutzapi.domain.entities.transaction.Transaction;
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

import java.math.BigDecimal;
import java.util.UUID;

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
    @DisplayName("Should return the created transaction")
    void createTransaction() throws Exception {
        BigDecimal amount = BigDecimal.valueOf(1);
        CreateTransactionDTO createTransactionDTO = new CreateTransactionDTO(amount, UUID.randomUUID(), UUID.randomUUID());
        Transaction transactionMock = mock(Transaction.class);

        when(transactionMock.getId()).thenReturn(1L);
        when(transactionMock.getAmount()).thenReturn(amount);
        when(transactionService.validateTransaction()).thenReturn(true);
        when(transactionService.createTransaction(createTransactionDTO)).thenReturn(transactionMock);

        ObjectMapper om = new ObjectMapper();
        String jsonTransaction = om.writeValueAsString(createTransactionDTO);
        this.mockMvc.perform(post("/transactions")
                        .content(jsonTransaction)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(amount.toString()));
        }
}