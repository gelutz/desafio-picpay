package com.lutzapi.application.services;

import com.lutzapi.domain.entities.transaction.Transaction;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.application.interfaces.ApiAdapter;
import com.lutzapi.application.adapters.MockyAdapter;
import com.lutzapi.application.dtos.MockyTransactionDTO;
import com.lutzapi.domain.exceptions.mocky.MockyAuthException;
import com.lutzapi.domain.exceptions.mocky.MockyDefaultExceptin;
import com.lutzapi.domain.exceptions.user.MissingInfoException;
import com.lutzapi.infrastructure.repositories.TransactionRepository;
import com.lutzapi.infrastructure.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
// essa anotação do lombok cria um constructor com os campos que não são final
// os campos no constructor são autowired por causa do IoC do spring, o que faz isso funcionar sem o @Autowired
@AllArgsConstructor
public class TransactionService {
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private UserService userService;
    private MockyAdapter apiAdapter;

    public Transaction createTransaction(TransactionDTO transaction) {
        List<String> emptyFields = new ArrayList<>();
        if (transaction.buyerId() == null) emptyFields.add("Buyer ID");
        if (transaction.sellerId() == null) emptyFields.add("Seller ID");
        if (transaction.amount() == null) emptyFields.add("Amount");

        if (!emptyFields.isEmpty()) throw new MissingInfoException(emptyFields);

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

    public void validateTransaction(User buyer, BigDecimal amount) {
        userService.validateUserForTransaction(buyer, amount);

        ResponseEntity<MockyTransactionDTO> response = apiAdapter.call();

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new MockyDefaultExceptin("Erro na transação");
        }


        if (!response.getBody().message().equals("Autorizado")) {
            throw new MockyAuthException("Você não está autorizado");
        }
    }
}
