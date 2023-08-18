package com.lutzapi.domain.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleEntityNotFound(EntityNotFoundException exception) {
        // TODO: adicionar um logger de verdade
        exception.printStackTrace();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ExceptionResponseDTO handleDefaultException(Exception exception) {
        // TODO: adicionar um logger de verdade
        exception.printStackTrace();
        return new ExceptionResponseDTO(exception.getMessage());
    }
}
