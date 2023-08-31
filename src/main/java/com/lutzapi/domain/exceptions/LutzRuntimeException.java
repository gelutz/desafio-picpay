package com.lutzapi.domain.exceptions;

public abstract class LutzRuntimeException extends RuntimeException {
    protected String message;
    public LutzRuntimeException(Long recordID) {

    }
}
