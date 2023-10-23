package com.lutzapi.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransactionDTO(UUID buyerId, UUID sellerId, BigDecimal amount) {
}
