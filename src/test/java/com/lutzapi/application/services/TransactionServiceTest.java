package com.lutzapi.application.services;

import com.lutzapi.application.adapters.MockyAdapter;
import com.lutzapi.application.dtos.MockyTransactionDTO;
import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.domain.exceptions.mocky.MockyAuthException;
import com.lutzapi.domain.exceptions.mocky.MockyDefaultExceptin;
import com.lutzapi.domain.exceptions.user.MissingInfoException;
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

import java.math.BigDecimal;

@SpringBootTest
public class TransactionServiceTest {
    private TransactionService sut;
    // TODO verificar o comportamento desses mocks criados com @Mock
    // talvez não é criado um novo objeto para cada teste, pode causar falhas entre testes
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
    @DisplayName("Caso dê algum problema com a Mocky API e não retornar status code 200")
    public void validationShouldThrowIfErrorInTransaction(){
        MockyTransactionDTO mockedDTO = new MockyTransactionDTO("Mocked message");
        ResponseEntity<MockyTransactionDTO> mockedResponseEntity = new ResponseEntity<>(mockedDTO, HttpStatus.BAD_REQUEST);

        Mockito.when(adapterMock.call()).thenReturn(mockedResponseEntity);
        Assertions.assertThrows(MockyDefaultExceptin.class, () -> sut.validateTransaction());
    }

    @Test
    @DisplayName("Deve lançar o erro MockyAuthException se não houver 'Autorizado' no response")
    public void validationShouldThrowIfNotAuthorized(){
        MockyTransactionDTO mockedDTO = new MockyTransactionDTO("Mocked message");
        ResponseEntity<MockyTransactionDTO> mockedResponseEntity = new ResponseEntity<>(mockedDTO, HttpStatus.OK);

        Mockito.when(adapterMock.call()).thenReturn(mockedResponseEntity);
        ResponseEntity<MockyTransactionDTO> result = adapterMock.call();

        Assertions.assertThrows(MockyAuthException.class, () -> sut.validateTransaction());
    }

    @Test
    @DisplayName("Não deve acontecer nada caso o usuário esteja autorizado")
    public void validationShouldPassIfAuthorized(){
        MockyTransactionDTO mockedDTO = new MockyTransactionDTO("Autorizado");
        ResponseEntity<MockyTransactionDTO> mockedResponseEntity = new ResponseEntity<>(mockedDTO, HttpStatus.OK);

        Mockito.when(adapterMock.call()).thenReturn(mockedResponseEntity);

        sut.validateTransaction();
        Mockito.verify(adapterMock, Mockito.times(1)).call();
    }

    @Test
    @DisplayName("Should throw If any data is missing (buyerId, sellerId, amount)")
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
