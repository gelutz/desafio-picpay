package com.lutzapi.domain.exceptions.handlers;

import com.lutzapi.domain.exceptions.LutzExceptionResponse;
import com.lutzapi.domain.exceptions.repository.NotFoundException;
import com.lutzapi.domain.exceptions.user.MissingDataException;
import org.apache.commons.lang3.exception.ExceptionUtils;
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

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

    protected void externalLog(Exception exception) {
        Logger LOGGER = LoggerFactory.getLogger(exception.getClass());
        System.out.println("-x-x-x:x-x-x-");
        LOGGER.error(ExceptionUtils.getStackTrace(exception));
        System.out.println("-x-x-x:x-x-x-");
    }

    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public LutzExceptionResponse handle(Exception exception) {
        externalLog(exception);
        return new LutzExceptionResponse(exception.getMessage(), null);
    }

    @ExceptionHandler(MissingDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public LutzExceptionResponse handle(MissingDataException exception) {
        externalLog(exception);
        return new LutzExceptionResponse(exception.getMessage(), exception.getFields());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public LutzExceptionResponse handle(DataIntegrityViolationException exception) {
        externalLog(exception);

        if (exception.getMessage().toLowerCase().contains("not-null")) {
            String field = exception.getMessage()
                    .substring(exception.getMessage().indexOf(":"));

            field = field.substring(field.lastIndexOf(".") + 1); // pega o campo que está nulo

            return new LutzExceptionResponse("O campo não pode ser nulo", field);
        }

        return new LutzExceptionResponse("Não foi possível gravar o registro.", null);
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
    public LutzExceptionResponse handle(MethodArgumentNotValidException exception) {
        externalLog(exception);

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new LutzExceptionResponse("Há erros com os valores enviados.", errors);
    }
}
