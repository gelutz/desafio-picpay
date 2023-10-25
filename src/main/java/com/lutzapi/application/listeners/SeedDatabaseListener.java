package com.lutzapi.application.listeners;

import com.lutzapi.application.services.SeedDatabaseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeedDatabaseListener {
    Logger logger = LoggerFactory.getLogger(getClass());

    final SeedDatabaseService seedDatabaseService;

    @Value("${lutzapi.seed}")
    private boolean seed;

    @EventListener
    public void seed(@SuppressWarnings("unused") ContextRefreshedEvent event) {
        if (seed) {
            long start = System.currentTimeMillis();
            seedDatabaseService.seed(500);
            long end = System.currentTimeMillis();
            System.out.println(end - start + "ms");
        }
    }
}
