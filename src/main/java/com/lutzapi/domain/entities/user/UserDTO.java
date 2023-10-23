package com.lutzapi.domain.entities.user;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UserDTO(String firstName, String lastName, String email, String document, UserType type, BigDecimal balance) {
}
