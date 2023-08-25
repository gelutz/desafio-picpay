package com.lutzapi.domain.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponseDTO handleDuplicateEntry(DataIntegrityViolationException exception) {
        ExceptionResponseDTO novaException = new ExceptionResponseDTO(exception.getMessage());
        externalLog(novaException);
        return novaException;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleEntityNotFound(EntityNotFoundException exception) {
        externalLog(exception);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ExceptionResponseDTO handleDefaultException(Exception exception) {
        ExceptionResponseDTO novaException = new ExceptionResponseDTO(exception.getMessage());
        externalLog(novaException);
        return novaException;
    }

    private void externalLog(Exception exception) {
        LOGGER.error("-x-x ERRO: " + exception.getClass().getName(), exception);
    }
}
