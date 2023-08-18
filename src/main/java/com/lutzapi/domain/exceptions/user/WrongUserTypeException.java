package com.lutzapi.domain.exceptions.user;

public class WrongUserTypeException extends Exception {
    public WrongUserTypeException(Long userId) {
        super("O usuário de id " + userId + " não é do tipo correto para essa operação.");
    }
}
