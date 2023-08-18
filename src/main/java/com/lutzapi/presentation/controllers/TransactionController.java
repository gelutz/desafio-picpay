package com.lutzapi.presentation.controllers;

import com.lutzapi.domain.entities.transaction.Transaction;
import com.lutzapi.application.dtos.TransactionDTO;
import com.lutzapi.application.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transaction) throws Exception {
            Transaction newTransactioon = transactionService.createTransaction(transaction);
            return new ResponseEntity<>(newTransactioon, HttpStatus.OK);
    }
}
