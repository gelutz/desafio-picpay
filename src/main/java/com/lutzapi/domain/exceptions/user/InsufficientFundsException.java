package com.lutzapi.domain.exceptions.user;

import java.util.UUID;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(UUID userId) {
        super("O usuário de id " + userId + " não possui saldo suficiente.");
    }
}
