package com.lutzapi.presentation.controllers;

import com.lutzapi.application.dtos.CreateTransactionDTO;
import com.lutzapi.application.dtos.FindTransactionDTO;
import com.lutzapi.application.services.TransactionService;
import com.lutzapi.domain.entities.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {
    private TransactionService transactionService;

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping
    public List<Transaction> findTransaction(@RequestBody FindTransactionDTO transactionDTO) {
        List<Transaction> transactions = transactionService.findAllByUserId(transactionDTO.userId());

        if (transactionDTO.transactionId() == null) {
            return transactions;
        }

        return transactions.stream().map((transaction) ->
                Objects.equals(transaction.getId(), transactionDTO.transactionId())
                        ? transaction
                        : null
        ).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping
    public Transaction createTransaction(@RequestBody CreateTransactionDTO transaction) {
            return transactionService.createTransaction(transaction);
    }
}
