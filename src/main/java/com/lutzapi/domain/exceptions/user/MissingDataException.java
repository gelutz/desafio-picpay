package com.lutzapi.domain.exceptions.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(of = "fields", callSuper = true)
public class MissingDataException extends RuntimeException {
    final List<String> fields;
    public MissingDataException(List<String> fields) {
        super("Os seguintes campos estão faltando");
        this.fields = fields;
    }
}
