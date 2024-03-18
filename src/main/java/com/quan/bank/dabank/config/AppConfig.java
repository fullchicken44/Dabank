package com.quan.bank.dabank.config;

import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }
}
