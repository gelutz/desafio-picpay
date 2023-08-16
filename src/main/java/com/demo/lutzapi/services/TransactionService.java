package com.demo.lutzapi.services;

import com.demo.lutzapi.domain.transaction.Transaction;
import com.demo.lutzapi.domain.user.User;
import com.demo.lutzapi.dtos.TransactionDTO;
import com.demo.lutzapi.repositories.TransactionRepository;
import com.demo.lutzapi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
// essa anotação do lombok cria um constructor com os campos que não são final
// os campos no constructor são autowired por causa do IoC do spring, o que faz isso funcionar sem o @Autowired
@AllArgsConstructor
public class TransactionService {
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private UserService userService;

    final RestTemplate restTemplate = new RestTemplate();
    final String apiUrl = "https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6";

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User buyer = userRepository.findById(transaction.buyerId())
                .orElseThrow(() -> new Exception("Usuário não encontrado"));
        User seller = userRepository.findById(transaction.sellerId())
                .orElseThrow(() -> new Exception("Usuário não encontrado"));

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

        ResponseEntity<Map> response = restTemplate
                .getForEntity(apiUrl, Map.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new Exception("Erro na transação");
        }

        // se o retorno for OK vai ter mensagem
        if (!response.getBody().get("message").toString().equals("Autorizado")) {
            throw new Exception("Você não está autorizado");
        }
    }
}
