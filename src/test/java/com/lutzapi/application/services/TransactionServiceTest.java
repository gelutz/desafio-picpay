package com.lutzapi.application.services;

import com.lutzapi.application.adapters.MockyAdapter;
import com.lutzapi.application.dtos.MockyTransactionDTO;
import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.domain.entities.transaction.Transaction;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserType;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import com.lutzapi.infrastructure.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

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

    // por algum motivo esse teste retorna null ao invés de retornar uma transaction
    @Test
    @DisplayName("Deve retornar uma transaction se a API validar")
    public void itShouldReturnTransactionIfValidated() {
        TransactionDTO transactionDTO = new TransactionDTO(BigDecimal.ONE, UUID.randomUUID(), UUID.randomUUID());

        User buyer = mock(User.class);
        when(buyer.getId()).thenReturn(transactionDTO.buyerId());
        when(buyer.getBalance()).thenReturn(transactionDTO.amount());
        when(buyer.getType()).thenReturn(UserType.BUYER);

        User seller = mock(User.class);
        when(seller.getId()).thenReturn(transactionDTO.sellerId());
        when(seller.getBalance()).thenReturn(transactionDTO.amount());
        when(seller.getType()).thenReturn(UserType.SELLER);

        when(userServiceMock.findById(transactionDTO.buyerId())).thenReturn(buyer);
        when(userServiceMock.findById(transactionDTO.sellerId())).thenReturn(seller);
        when(adapterMock.call()).thenReturn(new MockyTransactionDTO("Autorizado"));

        when(transactionRepoMock.save(any(Transaction.class))).thenReturn(mock(Transaction.class));
        Transaction response = sut.createTransaction(transactionDTO);

        assertInstanceOf(Transaction.class, response);
    }
}
