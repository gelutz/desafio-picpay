package com.lutzapi.infrastructure.dtos;

public record MockyTransactionDTO(String message) implements ApiDTO {
    @Override
    public String message() {
        return message != null ? message : "";
    }
}
