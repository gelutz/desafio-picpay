package com.lutzapi.domain.exceptions.handlers;

import com.lutzapi.domain.exceptions.LutzExceptionResponse;
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
        LOGGER.error("\n"
                + (new Date())
                + "-x-x ERRO: "
                + exception.getClass().getName());

    }

    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public LutzExceptionResponse handle(Exception exception) {
        externalLog(exception);
        return new LutzExceptionResponse(exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public LutzExceptionResponse handle(DataIntegrityViolationException exception) {
        String responseMessage = "Já existe um usuário com esse DOCUMENT";

        externalLog(exception);
        return new LutzExceptionResponse(responseMessage);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public LutzExceptionResponse handle(EntityNotFoundException exception) {
        externalLog(exception);
        return new LutzExceptionResponse(exception.getMessage());
    }

    @ExceptionHandler(MissingDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public LutzExceptionResponse handle(MissingDataException exception) {
        externalLog(exception);
        return new LutzExceptionResponse(exception.getMessage());
    }
}
