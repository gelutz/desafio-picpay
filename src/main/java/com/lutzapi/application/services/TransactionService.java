package com.lutzapi.application.services;

import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.application.gateways.APIGatewayDTO;
import com.lutzapi.application.gateways.FakeGateway;
import com.lutzapi.domain.entities.transaction.Transaction;
import com.lutzapi.domain.entities.user.User;
import com.lutzapi.domain.entities.user.UserType;
import com.lutzapi.domain.exceptions.repository.NotFoundException;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import com.lutzapi.domain.exceptions.user.WrongUserTypeException;
import com.lutzapi.infrastructure.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
// essa anotação do lombok cria um constructor com os campos que não são final
// os campos no constructor são autowired por causa do IoC do spring, o que faz isso funcionar sem o @Autowired
@AllArgsConstructor
public class TransactionService {
    private TransactionRepository transactionRepository;
    private UserService userService;
    private FakeGateway apiAdapter;

    public Transaction createTransaction(TransactionDTO transaction) {
        validateTransactionFields(transaction);

        User buyer = Optional.of(userService.findById(transaction.buyerId()))
                .orElseThrow(() -> new NotFoundException("buyer", transaction.buyerId()));
        User seller = Optional.of(userService.findById(transaction.sellerId()))
                .orElseThrow(() -> new NotFoundException("seller", transaction.sellerId()));

        if (buyer.getType() == UserType.SELLER)
            throw new WrongUserTypeException(buyer.getId());
        if (seller.getType() != UserType.SELLER)
            throw new WrongUserTypeException(buyer.getId());

        if (validateTransaction()) {
            Transaction newTransaction = new Transaction();
            newTransaction.setBuyer(buyer);
            newTransaction.setSeller(seller);
            newTransaction.setAmount(transaction.amount());

            userService.subtractBalance(buyer, transaction.amount());
            userService.addBalance(seller, transaction.amount());

            return transactionRepository.save(newTransaction);
        }

        return null;
    }

    public void validateTransactionFields(TransactionDTO transaction) {
        List<String> emptyFields = new ArrayList<>();
        if (transaction.buyerId() == null) emptyFields.add("Buyer ID");
        if (transaction.sellerId() == null) emptyFields.add("Seller ID");
        if (transaction.amount() == null) emptyFields.add("Amount");

        if (!emptyFields.isEmpty()) throw new MissingDataException(emptyFields);
    }

    public boolean validateTransaction() {
        APIGatewayDTO response = apiAdapter.call();
        return response.message().equals("Autorizado");
    }
}
