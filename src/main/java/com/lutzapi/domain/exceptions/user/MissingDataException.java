package com.lutzapi.domain.exceptions.user;

import java.util.List;

public class MissingDataException extends RuntimeException {
    public MissingDataException(List<String> fields) {
        super("Os seguintes campos estão faltando: " + String.join(", ", fields));
    }
}
