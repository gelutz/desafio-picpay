package com.lutzapi.domain.exceptions.user;

import java.util.UUID;

public class WrongUserTypeException extends RuntimeException {
    public WrongUserTypeException(UUID userId) {
        super("O usuário de id " + userId + " não é do tipo correto para essa operação.");
    }
}
