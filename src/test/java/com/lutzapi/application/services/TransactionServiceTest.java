package com.lutzapi.application.services;

import com.lutzapi.application.adapters.MockyAdapter;
import com.lutzapi.application.dtos.MockyTransactionDTO;
import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserType;
import com.lutzapi.domain.exceptions.mocky.MockyDefaultExceptin;
import com.lutzapi.domain.exceptions.user.MissingInfoException;
import com.lutzapi.infrastructure.repositories.TransactionRepository;
import com.lutzapi.infrastructure.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

@SpringBootTest
public class TransactionServiceTest {
    private TransactionService sut;
    private UserRepository userRepoMock;
    private TransactionRepository TransactionRepoMock;
    private UserService userServiceMock;
    private MockyAdapter adapterMock;

    @BeforeEach
    public void setUp() {
        adapterMock = Mockito.mock(MockyAdapter.class);

        userRepoMock = Mockito.mock(UserRepository.class);
        TransactionRepoMock = Mockito.mock(TransactionRepository.class);
        userServiceMock = Mockito.mock(UserService.class);
        sut = new TransactionService(userRepoMock, TransactionRepoMock, userServiceMock, adapterMock);
    }

    @Test
    public void createTransactionShouldThrowIfMissingData() {
        TransactionDTO transactionDTO = Mockito.mock(TransactionDTO.class);
        Assertions.assertThrows(MissingInfoException.class, () -> sut.createTransaction(transactionDTO));
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

        // buyerID
        Assertions.assertThrows(EntityNotFoundException.class, () -> sut.createTransaction(transactionDTO));
    }

    @Test
    public void nseiqoainda() {

    }






}
