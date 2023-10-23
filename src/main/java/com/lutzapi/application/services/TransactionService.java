package com.lutzapi.application.services;

import com.lutzapi.application.dtos.CreateTransactionDTO;
import com.lutzapi.application.gateways.APIGatewayDTO;
import com.lutzapi.application.gateways.FakeGateway;
import com.lutzapi.domain.entities.transaction.Transaction;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserType;
import com.lutzapi.domain.exceptions.repository.NotFoundException;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import com.lutzapi.domain.exceptions.user.WrongUserTypeException;
import com.lutzapi.infrastructure.repositories.TransactionRepository;
import com.lutzapi.infrastructure.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
// essa anotação do lombok cria um constructor com os campos que não são final
// os campos no constructor são autowired por causa do IoC do spring, o que faz isso funcionar sem o @Autowired
@AllArgsConstructor
public class TransactionService {
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private FakeGateway gateway;

    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Buyer ID", id + ""));
    }

    public List<Transaction> findAllByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found", userId));
        return null;
//        return transactionRepository.findAllByBuyerOrSeller(user);
    }

    public Transaction saveTransaction(CreateTransactionDTO transaction) {
        validateTransactionFields(transaction);

        User buyer = userRepository.findById(transaction.buyerId())
                .orElseThrow(() -> new NotFoundException("User not found", transaction.buyerId()));
        User seller = userRepository.findById(transaction.sellerId())
                .orElseThrow(() -> new NotFoundException("User not found", transaction.sellerId()));

        if (buyer.getType() == UserType.SELLER)
            throw new WrongUserTypeException(buyer.getId());
        if (seller.getType() != UserType.SELLER)
            throw new WrongUserTypeException(buyer.getId());

        if (validateTransaction()) {
            Transaction newTransaction = new Transaction();

            buyer.subtractBalance(transaction.amount());
            seller.addBalance(transaction.amount());

            newTransaction.setBuyer(buyer);
            newTransaction.setSeller(seller);
            newTransaction.setAmount(transaction.amount());

            return transactionRepository.save(newTransaction);
        }

        return null;
    }

    public void validateTransactionFields(CreateTransactionDTO transaction) {
        List<String> emptyFields = new ArrayList<>();
        if (transaction.buyerId() == null) emptyFields.add("Buyer ID");
        if (transaction.sellerId() == null) emptyFields.add("Seller ID");
        if (transaction.amount() == null) emptyFields.add("Amount");

        if (!emptyFields.isEmpty()) throw new MissingDataException(emptyFields);
    }

    public boolean validateTransaction() {
        APIGatewayDTO response = gateway.call();
        return response.message().equals("Autorizado");
    }
}
