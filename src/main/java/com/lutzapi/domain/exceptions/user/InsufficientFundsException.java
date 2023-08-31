package com.lutzapi.domain.exceptions.user;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(Long userId) {
        super("O usuário de id " + userId + " não possui saldo suficiente.");
    }
}
