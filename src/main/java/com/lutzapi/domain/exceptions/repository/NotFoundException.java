package com.lutzapi.domain.exceptions.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "key", callSuper = true)
public class NotFoundException extends EntityNotFoundException {
    String message;
    String key;
}
