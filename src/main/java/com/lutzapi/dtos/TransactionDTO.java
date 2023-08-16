package com.lutzapi.dtos;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal amount, Long sellerId, Long buyerId) {}
