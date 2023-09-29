package com.lutzapi.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BadDataException extends RuntimeException {
    String message;
}
