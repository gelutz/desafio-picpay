package com.lutzapi.application.services;

import com.lutzapi.application.adapters.MockyAdapter;
import com.lutzapi.application.dtos.MockyTransactionDTO;
import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.application.dtos.UserDTO;
import com.lutzapi.application.interfaces.ApiAdapter;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserType;
import com.lutzapi.domain.exceptions.mocky.MockyAuthException;
import com.lutzapi.domain.exceptions.mocky.MockyDefaultExceptin;
import com.lutzapi.domain.exceptions.user.MissingInfoException;
import com.lutzapi.infrastructure.repositories.TransactionRepository;
import com.lutzapi.infrastructure.repositories.UserRepository;
import com.sun.jdi.ObjectReference;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

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
    @DisplayName("The transactionn validator show throw an error if the user is not a buyer.")
    public void itShouldThrowIfUserIsNotBuyer(){
        User buyer = Instancio.create(User.class);
        buyer.setType(UserType.SELLER);
    }

    @Test
    @DisplayName("The transactionn validator show throw an error if the user doesn't have enough balance.")
    public void itShouldThrowIfUserDoesntHaveBalance(){
        Mockito.when(adapterMock.call()).thenReturn(new ResponseEntity<MockyTransactionDTO>(HttpStatus.OK));

        TransactionService ts = new TransactionService(userRepoMock, TransactionRepoMock, userServiceMock, adapterMock);

        BigDecimal userBalance = BigDecimal.valueOf(1);
        BigDecimal transactionAmount = userBalance.add(BigDecimal.valueOf(1));
        User buyer = Mockito.mock(User.class);
        buyer.setBalance(userBalance);

        Assertions.assertThrows(MockyDefaultExceptin.class, () -> sut.validateTransaction(buyer, transactionAmount));
    }


}
