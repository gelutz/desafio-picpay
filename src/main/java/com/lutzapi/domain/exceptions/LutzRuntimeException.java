package com.lutzapi.domain.exceptions;

public abstract class LutzRuntimeException extends RuntimeException {
    public LutzRuntimeException(String message, Long recordID) {
        super(message + recordID);
    }
}
