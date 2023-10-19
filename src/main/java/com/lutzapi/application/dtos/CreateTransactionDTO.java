package com.lutzapi.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransactionDTO(BigDecimal amount, UUID sellerId, UUID buyerId) {
}
