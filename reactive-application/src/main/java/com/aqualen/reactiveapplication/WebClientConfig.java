package com.aqualen.reactiveapplication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient webClient(@Value("${app.base.url}") String baseUrl){
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
