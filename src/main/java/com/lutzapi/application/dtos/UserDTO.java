package com.lutzapi.application.dtos;

import com.lutzapi.domain.entities.user.UserType;

import java.math.BigDecimal;

public record UserDTO(String firstName, String lastName, String email, String document, UserType type, BigDecimal balance) {
}
