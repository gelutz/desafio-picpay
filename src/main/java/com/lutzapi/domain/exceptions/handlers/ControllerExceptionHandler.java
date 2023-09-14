package com.lutzapi.domain.exceptions.handlers;

import com.lutzapi.domain.exceptions.LutzExceptionResponse;
import com.lutzapi.domain.exceptions.repository.NotFoundException;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {
    Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    protected void externalLog(Exception exception) {
        LOGGER.error((new Date()) + "-x-x ERRO: " + exception.getClass().getName() + "\n");
    }

    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public LutzExceptionResponse handle(Exception exception) {
        externalLog(exception);
        return new LutzExceptionResponse(exception.getMessage(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public LutzExceptionResponse handle(DataIntegrityViolationException exception) {
        externalLog(exception);
        return new LutzExceptionResponse("Já existe um registro com essas informações.", null);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public LutzExceptionResponse handle(NotFoundException exception) {
        externalLog(exception);
        return new LutzExceptionResponse("Não foi possível encontrar o registro com esse " + exception.getMessage(), exception.getKey());
    }

    @ExceptionHandler(MissingDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public LutzExceptionResponse handle(MissingDataException exception) {
        externalLog(exception);
        return new LutzExceptionResponse(exception.getMessage(), null);
    }
}
