package com.lutzapi.application.listeners;

import com.lutzapi.application.dtos.CreateTransactionDTO;
import com.lutzapi.application.services.TransactionService;
import com.lutzapi.application.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SeedDatabaseListener {
    final UserService userService;
    final TransactionService transactionService;

    @Value("${lutzapi.seed}")
    private String seed;

    final Logger logger = LoggerFactory.getLogger(getClass());

    @EventListener
    public void seed(@SuppressWarnings("unused") ContextRefreshedEvent event) {
        if (Boolean.parseBoolean(seed)) {
            List<CreateTransactionDTO> dtos = userService.seed(100);
            dtos.forEach((dto) -> {
                try {
                    transactionService.saveTransaction(dto);
                } catch (Exception e) {
                    logger.info("Houve um erro ao criar a transação: " + e.getMessage());
                }
            });
        }
    }
}
