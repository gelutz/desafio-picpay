package com.lutzapi.dtos;

import com.lutzapi.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO(String firstName, String lastName, String email, String document, UserType type, BigDecimal balance) {
}
