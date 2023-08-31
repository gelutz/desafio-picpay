package com.lutzapi.domain.exceptions.user;

import java.util.List;

public class MissingInfoException extends RuntimeException {
    public MissingInfoException(List<String> fields) {
        super("Os seguintes campos estão faltando: " + String.join(", ", fields));
    }
}
