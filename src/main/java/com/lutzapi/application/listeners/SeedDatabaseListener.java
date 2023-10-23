package com.lutzapi.application.listeners;

import com.lutzapi.application.dtos.CreateTransactionDTO;
import com.lutzapi.application.services.TransactionService;
import com.lutzapi.application.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class SeedDatabaseListener {
    UserService userService;
    TransactionService transactionService;
    Environment environment;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        if (environment.acceptsProfiles(Profiles.of("default"))) {
            List<CreateTransactionDTO> dtos = userService.seed(10);
            System.out.println(dtos.toString());
            dtos.forEach(transactionService::saveTransaction);
        }
    }
}
