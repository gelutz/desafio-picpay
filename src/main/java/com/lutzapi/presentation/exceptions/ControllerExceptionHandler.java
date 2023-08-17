package com.lutzapi.presentation.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseDTO> handleDuplicateEntry(DataIntegrityViolationException exception) {
        // TODO: adicionar um logger de verdade
        exception.printStackTrace();
        return ResponseEntity.badRequest()
                .body(new ExceptionResponseDTO(exception.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleEntityNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO> handleDefaultException(Exception exception) {
        // TODO: adicionar um logger de verdade
        exception.printStackTrace();
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponseDTO(exception.getMessage()));
    }
}
