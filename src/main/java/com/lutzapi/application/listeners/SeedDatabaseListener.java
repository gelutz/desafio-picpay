package com.lutzapi.application.listeners;

import com.lutzapi.application.services.SeedDatabaseService;
import com.lutzapi.domain.entities.user.User;
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
    Logger logger = LoggerFactory.getLogger(getClass());

    final SeedDatabaseService seedDatabaseService;

    @Value("${lutzapi.seed}")
    private String seed;

    @EventListener
    public void seed(@SuppressWarnings("unused") ContextRefreshedEvent event) {
        if (Boolean.parseBoolean(seed)) {
            long start = System.currentTimeMillis();
            List<User> users = seedDatabaseService.seedUsers(1000);
            seedDatabaseService.seedTransactions(users);
            long end = System.currentTimeMillis();
            System.out.println(end - start + "ms");
        }
    }
}
