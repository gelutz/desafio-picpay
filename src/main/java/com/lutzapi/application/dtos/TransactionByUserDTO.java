package com.lutzapi.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionByUserDTO(UUID buyerId, UUID sellerId, BigDecimal amount) {
}
