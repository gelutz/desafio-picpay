package com.lutzapi.advices;

import com.lutzapi.dtos.LExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<LExceptionDTO> handleDuplicateEntry(DataIntegrityViolationException divException) {
        divException.printStackTrace();
        LExceptionDTO lexception = new LExceptionDTO(divException.getMessage(), 401);

        return ResponseEntity.badRequest().body(lexception);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleEntityNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<LExceptionDTO> handleDefaultException(Exception exception) {
        exception.printStackTrace();
        LExceptionDTO lException = new LExceptionDTO(exception.getMessage(), 500);

        return ResponseEntity.internalServerError().body(lException);
    }
}
