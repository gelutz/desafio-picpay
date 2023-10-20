package com.lutzapi.application.services;

import com.lutzapi.application.dtos.CreateTransactionDTO;
import com.lutzapi.application.gateways.APIGatewayDTO;
import com.lutzapi.application.gateways.FakeGateway;
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
import java.util.Optional;
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
    private FakeGateway adapterMock;

    @BeforeEach
    public void setUp() {
        sut = new TransactionService(transactionRepoMock, userServiceMock, adapterMock);
    }

    @Test
    @DisplayName("Deve buscar uma transaction pelo ID dela")
    public void itShouldFindATransaction() {
        Transaction mockedTransaction = Transaction.builder()
                .amount(BigDecimal.ONE)
                .build();
        when(transactionRepoMock.findById(anyLong())).thenReturn(Optional.of(mockedTransaction));

        Transaction found = sut.findById(1L);
        assertEquals(found.getAmount(), mockedTransaction.getAmount());
    }

    @Test
    @DisplayName("Deve lançar uma MissingDataException com cada campo que falta (buyerId, sellerId, amount)")
    public void itShouldThrowIfMissingData() {
        CreateTransactionDTO createTransactionDTO = new CreateTransactionDTO(null, null, null);

        MissingDataException exception = assertThrows(MissingDataException.class,
                () -> sut.validateTransactionFields(createTransactionDTO));

        assertTrue(exception.getFields().contains("Amount"));
        assertTrue(exception.getFields().contains("Buyer ID"));
        assertTrue(exception.getFields().contains("Seller ID"));
    }

    // por algum motivo esse teste retorna null ao invés de retornar uma transaction
    @Test
    @DisplayName("Deve retornar uma transaction se a API validar")
    public void itShouldReturnTransactionIfValidated() {
        CreateTransactionDTO createTransactionDTO = new CreateTransactionDTO(BigDecimal.ONE, UUID.randomUUID(), UUID.randomUUID());

        User buyer = mock(User.class);
        when(buyer.getId()).thenReturn(createTransactionDTO.buyerId());
        when(buyer.getBalance()).thenReturn(createTransactionDTO.amount());
        when(buyer.getType()).thenReturn(UserType.BUYER);

        User seller = mock(User.class);
        when(seller.getId()).thenReturn(createTransactionDTO.sellerId());
        when(seller.getBalance()).thenReturn(createTransactionDTO.amount());
        when(seller.getType()).thenReturn(UserType.SELLER);

        when(userServiceMock.findById(createTransactionDTO.buyerId())).thenReturn(buyer);
        when(userServiceMock.findById(createTransactionDTO.sellerId())).thenReturn(seller);
        when(adapterMock.call()).thenReturn(new APIGatewayDTO("Autorizado"));

        when(transactionRepoMock.save(any(Transaction.class))).thenReturn(mock(Transaction.class));
        Transaction response = sut.createTransaction(createTransactionDTO);

        assertInstanceOf(Transaction.class, response);
    }
}
