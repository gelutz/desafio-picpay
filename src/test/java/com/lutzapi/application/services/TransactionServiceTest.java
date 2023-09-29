package com.lutzapi.application.services;

import com.lutzapi.application.adapters.MockyAdapter;
import com.lutzapi.application.dtos.MockyTransactionDTO;
import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.domain.entities.transaction.Transaction;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import com.lutzapi.infrastructure.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class TransactionServiceTest {
    private TransactionService sut;
    @Mock
    private TransactionRepository transactionRepoMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private MockyAdapter adapterMock;

    @BeforeEach
    public void setUp() {
        sut = new TransactionService(transactionRepoMock, userServiceMock, adapterMock);
    }

    @Test
    @DisplayName("Deve lançar uma MissingDataException com cada campo que falta (buyerId, sellerId, amount)")
    public void itShouldThrowIfMissingData() {
        TransactionDTO transactionDTO = new TransactionDTO(null, null, null);

        MissingDataException exception = assertThrows(MissingDataException.class,
                () -> sut.validateTransactionFields(transactionDTO));

        assertTrue(exception.getFields().contains("Amount"));
        assertTrue(exception.getFields().contains("Buyer ID"));
        assertTrue(exception.getFields().contains("Seller ID"));
    }

    @Test
    @DisplayName("Deve retornar nulo se a API não validar")
    public void itShouldReturnNullIfNotValidated() {
        TransactionDTO transactionDTO = new TransactionDTO(BigDecimal.ONE, 1L, 2L);

        when(userServiceMock.findById(anyLong())).thenReturn(mock(User.class));
        when(adapterMock.call()).thenReturn(new MockyTransactionDTO("Mocked message"));

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

        when(transactionRepoMock.save(any(Transaction.class))).thenReturn(mock(Transaction.class));

        Transaction response = sut.saveTransaction(buyer, seller, transactionDTO);

        assertInstanceOf(Transaction.class, response);
    }


    // sem querer esse teste também testa a função saveTransaction
    // TODO ver se tem o que fazer pra esse teste não testar a função saveTransaction
    @Test
    @DisplayName("Deve retornar uma transaction se a API validar")
    public void itShouldReturnTransactionIfValidated() {
        TransactionDTO transactionDTO = new TransactionDTO(BigDecimal.ONE, 1L, 2L);
        User buyer = mock(User.class);
        User seller = mock(User.class);
        when(buyer.getId()).thenReturn(transactionDTO.buyerId());
        when(buyer.getBalance()).thenReturn(transactionDTO.amount());
        when(seller.getId()).thenReturn(transactionDTO.sellerId());
        when(seller.getBalance()).thenReturn(transactionDTO.amount());

        when(userServiceMock.findById(transactionDTO.buyerId())).thenReturn(buyer);
        when(userServiceMock.findById(transactionDTO.sellerId())).thenReturn(seller);

        when(adapterMock.call()).thenReturn(new MockyTransactionDTO("Autorizado"));

        Transaction response = sut.createTransaction(transactionDTO);

        assertInstanceOf(Transaction.class, response);
    }
}
