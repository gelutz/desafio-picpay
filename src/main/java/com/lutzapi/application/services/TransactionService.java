package com.lutzapi.application.services;

import com.lutzapi.application.adapters.MockyAdapter;
import com.lutzapi.application.dtos.MockyTransactionDTO;
import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.domain.entities.transaction.Transaction;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserType;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import com.lutzapi.domain.exceptions.user.WrongUserTypeException;
import com.lutzapi.infrastructure.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
// essa anotação do lombok cria um constructor com os campos que não são final
// os campos no constructor são autowired por causa do IoC do spring, o que faz isso funcionar sem o @Autowired
@AllArgsConstructor
public class TransactionService {
    private TransactionRepository transactionRepository;
    private UserService userService;
    private MockyAdapter apiAdapter;

    public Transaction createTransaction(TransactionDTO transaction) {
        validateTransactionFields(transaction);

        User buyer = userService.findById(transaction.buyerId());
        User seller = userService.findById(transaction.sellerId());

        if (buyer.getType() == UserType.SELLER) {
            throw new WrongUserTypeException(buyer.getId());
        }

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

        userService.subtractBalance(buyer, transaction.amount());
        userService.addBalance(seller, transaction.amount());

        return transactionRepository.save(newTransaction);
    }

    public boolean validateTransaction() {
        MockyTransactionDTO response = apiAdapter.call();
        return response.message().equals("Autorizado");
    }

    public void validateTransactionFields(TransactionDTO transaction) {
        List<String> emptyFields = new ArrayList<>();
        if (transaction.buyerId() == null) emptyFields.add("Buyer ID");
        if (transaction.sellerId() == null) emptyFields.add("Seller ID");
        if (transaction.amount() == null) emptyFields.add("Amount");

        if (!emptyFields.isEmpty()) throw new MissingDataException(emptyFields);
    }
}
