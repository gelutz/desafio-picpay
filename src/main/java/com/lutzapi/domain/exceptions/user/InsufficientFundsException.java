package com.lutzapi.domain.exceptions.user;

import java.math.BigDecimal;
import java.util.UUID;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(UUID userId) {
        super("O usuário de id " + userId + " não possui saldo suficiente.");
    }

    public InsufficientFundsException(UUID userId, BigDecimal amount, BigDecimal balance) {
        super("O usuário de id " + userId + " não possui saldo suficiente. Amount: " + amount + "; Balance: " + balance);
    }
}
