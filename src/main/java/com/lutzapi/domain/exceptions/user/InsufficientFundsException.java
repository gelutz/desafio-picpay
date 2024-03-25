package com.lutzapi.domain.exceptions.user;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(Long userId) {
        super("O usuário de id " + userId + " não possui saldo suficiente.");
    }

    public InsufficientFundsException(Long userId, BigDecimal amount, BigDecimal balance) {
        super("O usuário de id " + userId + " não possui saldo suficiente. Amount: " + amount + "; Balance: " + balance);
    }
}
