package com.lutzapi.domain.exceptions.handlers;

import com.lutzapi.domain.exceptions.LutzExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler extends LutzExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public LutzExceptionResponse handleDuplicateEntry(DataIntegrityViolationException exception) {
        LutzExceptionResponse novaException = new LutzExceptionResponse(exception.getMessage());
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
    public LutzExceptionResponse handleDefaultException(Exception exception) {
        LutzExceptionResponse novaException = new LutzExceptionResponse(exception.getMessage());
        externalLog(novaException);
        return novaException;
    }
}
