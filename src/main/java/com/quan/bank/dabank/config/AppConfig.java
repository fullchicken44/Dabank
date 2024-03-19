package com.quan.bank.dabank.config;

import com.google.common.eventbus.EventBus;
import com.quan.bank.dabank.aggregates.EventManager;
import com.quan.bank.dabank.aggregates.EventStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }

    @Bean
    public EventManager eventManager(EventBus eventBus, EventStorage eventStorage) {
        EventManager eventManager = new EventManager(eventBus, eventStorage);
        eventBus.register(eventManager);
        return eventManager;
    }
}
