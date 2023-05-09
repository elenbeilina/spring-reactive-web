package com.aqualen.reactiveapplication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quote")
public class ReactiveController {

    private final WebClient webClient;

    @GetMapping
    Mono<String> getQuote() {
        return webClient.get()
                .uri("/")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/delay")
    Mono<String> getQuoteWithDelay() {
        LocalDateTime before = LocalDateTime.now();
        return webClient
                .get()
                .uri("/delay")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> {
                    LocalDateTime after = LocalDateTime.now();
                    log.info("Time of the call took {} millis.\n", ChronoUnit.MILLIS.between(before, after));
                });
    }

    @GetMapping("/error")
    Mono<String> getError() {
        return webClient
                .get()
                .uri("/error")
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(response -> {
                    if (response.statusCode() == HttpStatus.OK) {
                        return response.bodyToMono(String.class);
                    }
                    return response.bodyToMono(String.class)
                            .doOnNext(responseError -> log.error("Error happened {}!", responseError));
                });
    }


}
