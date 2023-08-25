package com.lutzapi.application.services;

import com.lutzapi.domain.entities.transaction.Transaction;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.application.interfaces.ApiAdapter;
import com.lutzapi.application.adapters.MockyAdapter;
import com.lutzapi.application.dtos.MockyTransactionDTO;
import com.lutzapi.domain.exceptions.mocky.MockyAuthException;
import com.lutzapi.domain.exceptions.mocky.MockyDefaultExceptin;
import com.lutzapi.infrastructure.repositories.TransactionRepository;
import com.lutzapi.infrastructure.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
// essa anotação do lombok cria um constructor com os campos que não são final
// os campos no constructor são autowired por causa do IoC do spring, o que faz isso funcionar sem o @Autowired
@AllArgsConstructor
public class TransactionService {
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private UserService userService;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User buyer = userRepository.findById(transaction.buyerId())
                .orElseThrow(EntityNotFoundException::new);
        User seller = userRepository.findById(transaction.sellerId())
                .orElseThrow(EntityNotFoundException::new);

        validateTransaction(buyer, transaction.amount());

        Transaction newTransaction = new Transaction();
        newTransaction.setBuyer(buyer);
        newTransaction.setSeller(seller);
        newTransaction.setAmount(transaction.amount());

        buyer.setBalance(buyer.getBalance().subtract(transaction.amount()));
        seller.setBalance(seller.getBalance().add(transaction.amount()));

        userRepository.save(buyer);
        userRepository.save(seller);
        transactionRepository.save(newTransaction);

        return newTransaction;
    }

    public void validateTransaction(User buyer, BigDecimal amount) throws Exception {
        userService.validateTransaction(buyer, amount);

        ApiAdapter api = new MockyAdapter();
        ResponseEntity<MockyTransactionDTO> response = api.call();

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new MockyDefaultExceptin("Erro na transação");
        }

        // se a resposta for OK vai ter body e mensagem
        assert response.getBody() != null;

        if (!response.getBody().message().equals("Autorizado")) {
            throw new MockyAuthException("Você não está autorizado");
        }
    }
}
