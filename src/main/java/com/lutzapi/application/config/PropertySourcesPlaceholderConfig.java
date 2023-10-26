package com.lutzapi.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public class PropertySourcesPlaceholderConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer p = new PropertySourcesPlaceholderConfigurer();
        // essa config faz o @Value escolher o valor default caso não ecnontre a property no .properties
        // ex: @Value("${lutzapi.seed:false}) -> se não encontrar o lutzapi.seed, seta como false
        p.setIgnoreResourceNotFound(true);
        return p;
    }
}
