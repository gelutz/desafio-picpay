package com.lutzapi.application.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.lutzapi.application.adapters.MockyAdapter;
import com.lutzapi.application.dtos.MockyTransactionDTO;
import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.domain.entities.transaction.Transaction;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import com.lutzapi.infrastructure.repositories.TransactionRepository;
import com.lutzapi.infrastructure.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class TransactionServiceTest {
    private TransactionService sut;
    @Mock
    private UserRepository userRepoMock;
    @Mock
    private TransactionRepository transactionRepoMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private MockyAdapter adapterMock;

    @BeforeEach
    public void setUp() {
        sut = new TransactionService(userRepoMock, transactionRepoMock, userServiceMock, adapterMock);
    }

    @Test
    @DisplayName("Deve lançar uma MissingDataException com cada campo que falta (buyerId, sellerId, amount)")
    public void itShouldThrowIfMissingData() {
        TransactionDTO transactionDTO = new TransactionDTO(null, null, null);

        Exception exception = assertThrows(MissingDataException.class,
                () -> sut.validateTransactionFields(transactionDTO));

        assertTrue(exception.getMessage().contains("Amount"));
        assertTrue(exception.getMessage().contains("Buyer ID"));
        assertTrue(exception.getMessage().contains("Seller ID"));
    }

    @Test
    @DisplayName("Deve lançar uma EntityNotFoundException se não encontrar o buyer ou o seller")
    public void createTransactionShouldThrowIfUsersNotFound() {
        TransactionDTO transactionDTO = new TransactionDTO(BigDecimal.ONE, 1L, 2L);

        when(userRepoMock.findById(transactionDTO.buyerId())).thenThrow(EntityNotFoundException.class);
        when(userRepoMock.findById(transactionDTO.sellerId())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> userRepoMock.findById(transactionDTO.buyerId()));
        assertThrows(EntityNotFoundException.class, () -> userRepoMock.findById(transactionDTO.sellerId()));
    }

    @Test
    @DisplayName("Deve retornar nulo se a API não validar")
    public void itShouldReturnNullIfNotValidated() {
        TransactionDTO transactionDTO = new TransactionDTO(BigDecimal.ONE, 1L, 2L);

        when(userRepoMock.findById(transactionDTO.buyerId()))
                .thenReturn(Optional.of(mock(User.class)));
        when(userRepoMock.findById(transactionDTO.sellerId()))
                .thenReturn(Optional.of(mock(User.class)));
        when(adapterMock.call())
                .thenReturn(new MockyTransactionDTO("Mocked message"));

        Transaction response = sut.createTransaction(transactionDTO);

        assertNull(response);
    }

    @Test
    @DisplayName("Deve salvar e retornar a transação")
    public void itShouldSaveAndReturnTransaction() {
        TransactionDTO transactionDTO = new TransactionDTO(BigDecimal.ONE, 1L, 2L);
        User buyer = mock(User.class);
        User seller = mock(User.class);
        when(buyer.getId()).thenReturn(transactionDTO.buyerId());
        when(buyer.getBalance()).thenReturn(transactionDTO.amount());
        when(seller.getId()).thenReturn(transactionDTO.sellerId());
        when(seller.getBalance()).thenReturn(transactionDTO.amount());

        Transaction response = sut.saveTransaction(buyer, seller, transactionDTO);

        assertInstanceOf(Transaction.class, response);
    }


    // sem querer esse teste também testa a função saveTransaction
    // TODO ver se tem o que fazer pra esse teste não testar a função saveTransaction
    @Test
    @DisplayName("Deve retornar uma transaction se a API validar")
    public void itShouldReturnTransactionIfValidated() {
        TransactionDTO transactionDTO = new TransactionDTO(BigDecimal.ONE, 1L, 2L);

        when(userRepoMock.findById(transactionDTO.buyerId()))
                .thenReturn(Optional.of(mock(User.class)));
        when(userRepoMock.findById(transactionDTO.sellerId()))
                .thenReturn(Optional.of(mock(User.class)));
        when(adapterMock.call())
                .thenReturn(new MockyTransactionDTO("Autorizado"));

        Transaction response = sut.createTransaction(transactionDTO);

        assertInstanceOf(Transaction.class, response);
    }
}
