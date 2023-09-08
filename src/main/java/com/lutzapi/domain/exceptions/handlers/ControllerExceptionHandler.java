package com.lutzapi.domain.exceptions.handlers;

import com.lutzapi.domain.exceptions.LutzExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    protected void externalLog(Exception exception) {
        LOGGER.error("\n-x-x ERRO: " + exception.getClass().getName(), exception);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public LutzExceptionResponse handleDuplicateEntry(DataIntegrityViolationException exception) {
        externalLog(exception);
        return new LutzExceptionResponse(exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public LutzExceptionResponse handleEntityNotFound(EntityNotFoundException exception) {
        externalLog(exception);
        return new LutzExceptionResponse(exception.getMessage());

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public LutzExceptionResponse handleDefaultException(Exception exception) {
        externalLog(exception);
        return new LutzExceptionResponse(exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public LutzExceptionResponse handleDefaultException(RuntimeException exception) {
        externalLog(exception);
        return new LutzExceptionResponse(exception.getMessage());
    }
}
