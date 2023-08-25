package com.lutzapi.domain.exceptions;

import lombok.AllArgsConstructor;


public class ExceptionResponseDTO extends Exception  {
    public ExceptionResponseDTO() {
    }

    public ExceptionResponseDTO(String message) {
        super(message);
    }

    public ExceptionResponseDTO(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceptionResponseDTO(Throwable cause) {
        super(cause);
    }

    public ExceptionResponseDTO(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
