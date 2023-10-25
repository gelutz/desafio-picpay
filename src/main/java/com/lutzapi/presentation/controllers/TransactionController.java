package com.lutzapi.presentation.controllers;

import com.lutzapi.application.dtos.CreateTransactionDTO;
import com.lutzapi.application.dtos.TransactionByUserDTO;
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
    @GetMapping("/{id}")
    public Transaction findTransaction(@PathVariable Long id) {
        return transactionService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping
    public Transaction createTransaction(@RequestBody CreateTransactionDTO transaction) {
        return transactionService.saveTransactionFromDTO(transaction);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("/history/{userId}")
    public List<TransactionByUserDTO> history(@PathVariable UUID userId) {
        return transactionService.findAllByUserId(userId);
    }
}
