package com.lutzapi.domain.exceptions;


public class LutzExceptionResponse extends Exception  {
    public LutzExceptionResponse() {
    }

    public LutzExceptionResponse(String message) {
        super(message);
    }

    public LutzExceptionResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public LutzExceptionResponse(Throwable cause) {
        super(cause);
    }

    public LutzExceptionResponse(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
