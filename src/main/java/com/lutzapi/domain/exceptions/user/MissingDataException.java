package com.lutzapi.domain.exceptions.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode(of = "fields", callSuper = true)
@ToString
@Getter
public class MissingDataException extends RuntimeException {
    final List<String> fields;
    public MissingDataException(List<String> fields) {
        super("Os seguintes campos estão faltando");
        this.fields = fields;
    }
}
