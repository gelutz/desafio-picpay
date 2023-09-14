package com.lutzapi.domain.exceptions.user;

import java.util.List;
import java.util.Objects;

public class MissingDataException extends RuntimeException {
    final List<String> requiredFields;
    public MissingDataException(List<String> fields) {
        super("Os seguintes campos estão faltando: " + String.join(", ", fields));
        requiredFields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MissingDataException that = (MissingDataException) o;
        return Objects.equals(requiredFields, that.requiredFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requiredFields);
    }
}
