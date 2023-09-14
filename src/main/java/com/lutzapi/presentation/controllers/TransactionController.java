package com.lutzapi.presentation.controllers;

import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.application.services.TransactionService;
import com.lutzapi.domain.entities.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {
    private TransactionService transactionService;

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping
    public Transaction createTransaction(@RequestBody TransactionDTO transaction) {
            return transactionService.createTransaction(transaction);
    }
}
