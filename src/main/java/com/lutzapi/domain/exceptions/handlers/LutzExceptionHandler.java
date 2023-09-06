package com.lutzapi.domain.exceptions.handlers;

import com.lutzapi.domain.exceptions.handlers.ControllerExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public abstract class LutzExceptionHandler {
    Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    protected void externalLog(Exception exception) {
        LOGGER.error("-x-x ERRO: " + exception.getClass().getName(), exception);
    }
}
