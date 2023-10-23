package com.lutzapi.application.listeners;

import com.lutzapi.application.services.TransactionService;
import com.lutzapi.application.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class SeedDatabaseListener {
    UserService userService;
    TransactionService transactionService;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        userService.seed(10);
    }
}
