package com.lutzapi.domain.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BadDataException extends RuntimeException {
    String message;
}
