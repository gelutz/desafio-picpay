package com.lutzapi.application.services;

import com.lutzapi.domain.entities.transaction.Transaction;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.application.adapters.MockyAdapter;
import com.lutzapi.application.dtos.MockyTransactionDTO;
import com.lutzapi.domain.exceptions.mocky.MockyAuthException;
import com.lutzapi.domain.exceptions.mocky.MockyDefaultExceptin;
import com.lutzapi.domain.exceptions.repository.NotFoundException;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import com.lutzapi.infrastructure.repositories.TransactionRepository;
import com.lutzapi.infrastructure.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

        if (!emptyFields.isEmpty()) throw new MissingDataException(emptyFields);

        User buyer = userRepository.findById(transaction.buyerId())
                .orElseThrow(() -> new NotFoundException("Buyer ID", transaction.buyerId().toString()));
        User seller = userRepository.findById(transaction.sellerId())
                .orElseThrow(() -> new NotFoundException("Seller ID", transaction.sellerId().toString()));

        userService.validateUserForTransaction(buyer, transaction.amount());

        if (validateTransaction()) {
            return saveTransaction(buyer, seller, transaction);
        }

        return null;
    }

    public Transaction saveTransaction(User buyer, User seller, TransactionDTO transaction) {
        Transaction newTransaction = new Transaction();
        newTransaction.setBuyer(buyer);
        newTransaction.setSeller(seller);
        newTransaction.setAmount(transaction.amount());

        buyer.subtractBalance(transaction.amount());
        seller.addBalance(transaction.amount());

        userRepository.save(buyer);
        userRepository.save(seller);
        return transactionRepository.save(newTransaction);
    }

    public boolean validateTransaction() {
        MockyTransactionDTO response = apiAdapter.call();
        return response.message().equals("Autorizado");
    }
}
