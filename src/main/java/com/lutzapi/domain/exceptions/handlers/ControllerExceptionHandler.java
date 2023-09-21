package com.lutzapi.domain.exceptions.handlers;

import com.lutzapi.domain.exceptions.LutzExceptionResponse;
import com.lutzapi.domain.exceptions.repository.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler(HttpClientErrorException.class)
    public LutzExceptionResponse handle(HttpClientErrorException exception) {
        externalLog(exception);
        return new LutzExceptionResponse("API do Mocky retornou 404", null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    // TODO alterar esse retorno para um LutzExceptionResponse
    public Map<String, String> handle(MethodArgumentNotValidException exception) {
        externalLog(exception);

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
