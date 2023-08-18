package com.lutzapi.application.dtos;

import com.lutzapi.application.interfaces.ApiDTO;

public record MockyTransactionDTO(String message) implements ApiDTO {
    @Override
    public String message() {
        return message != null ? message : "";
    }
}
