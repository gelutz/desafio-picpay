package com.lutzapi.domain.entities.transaction;

import com.lutzapi.domain.entities.user.User;

import java.math.BigDecimal;

public record TransactionDTO(User buyer, User seller, BigDecimal amount) {
}
