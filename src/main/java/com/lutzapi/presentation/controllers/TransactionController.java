package com.lutzapi.presentation.controllers;

import com.lutzapi.application.dtos.CreateTransactionDTO;
import com.lutzapi.application.services.TransactionService;
import com.lutzapi.domain.entities.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {
    private TransactionService transactionService;

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping()
    public List<Transaction> transactionHistory(@RequestBody UUID userId) {
        return transactionService.findAllByUserId(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping
    public Transaction createTransaction(@RequestBody CreateTransactionDTO transaction) {
        return transactionService.saveTransaction(transaction);
    }
}
