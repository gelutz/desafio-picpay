package com.demo.lutzapi.dtos;

import com.demo.lutzapi.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO(String firstName, String lastName, String email, String document, UserType type, BigDecimal balance) {
}
