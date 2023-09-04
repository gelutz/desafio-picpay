package com.lutzapi.application.services;

import com.lutzapi.application.adapters.MockyAdapter;
import com.lutzapi.application.dtos.MockyTransactionDTO;
import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.domain.exceptions.mocky.MockyAuthException;
import com.lutzapi.domain.exceptions.mocky.MockyDefaultExceptin;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import com.lutzapi.infrastructure.repositories.TransactionRepository;
import com.lutzapi.infrastructure.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@SpringBootTest
@ActiveProfiles("test")
public class TransactionServiceTest {
    private TransactionService sut;
    @Mock
    private UserRepository userRepoMock;
    @Mock
    private TransactionRepository TransactionRepoMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private MockyAdapter adapterMock;

    @BeforeEach
    public void setUp() {
        sut = new TransactionService(userRepoMock, TransactionRepoMock, userServiceMock, adapterMock);
    }

    @Test
    @DisplayName("Should throw If any data is missing (buyerId, sellerId, amount)")
    public void createTransactionShouldThrowIfMissingData() {
        TransactionDTO transactionDTO = new TransactionDTO(null, null, null);

        Exception exception = Assertions.assertThrows(MissingDataException.class,
                () -> sut.createTransaction(transactionDTO));

        Assertions.assertTrue(exception.getMessage().contains("Amount"));
        Assertions.assertTrue(exception.getMessage().contains("Buyer ID"));
        Assertions.assertTrue(exception.getMessage().contains("Seller ID"));
    }

    // esse teste vai falhar quando a base for trocada de um H2 para uma com persistência
    // TODO descobrir como mockar o banco utilizado ou usar outra instância do H2
    @Test
    public void createTransactionShouldThrowIfUsersNotFound() {
        TransactionDTO transactionDTO = Mockito.mock(TransactionDTO.class);
        Mockito.when(transactionDTO.amount()).thenReturn(BigDecimal.ONE);
        Mockito.when(transactionDTO.sellerId()).thenReturn(1L);
        Mockito.when(transactionDTO.buyerId()).thenReturn(1L);

        Mockito.when(userRepoMock.findById(Mockito.anyLong())).thenThrow(EntityNotFoundException.class);
    }
}
