package com.lutzapi.application.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FindTransactionDTO(@NotNull UUID userId, Long transactionId) {
}
