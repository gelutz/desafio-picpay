package com.lutzapi.application.dtos;

import com.lutzapi.domain.entities.user.UserType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UserDTO(String firstName, String lastName, String email, String document, UserType type, BigDecimal balance) {
}
