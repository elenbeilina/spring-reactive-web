package com.aqualen.reactiveapplication;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/api")
public class SubscriberController {

    private final Sinks.Many<String> quotes;
    private final AtomicLong counter;

    public SubscriberController() {
        this.quotes = Sinks.many().multicast().onBackpressureBuffer();
        this.counter = new AtomicLong();
        addNewQuote();
    }

    @PutMapping("quote")
    public void addQuote() {
        addNewQuote();
    }

    @GetMapping(value = "quotes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getQuotes() {
        return quotes.asFlux();
    }

    private void addNewQuote() {
        long nextNumber = counter.getAndIncrement();
        Sinks.EmitResult result = quotes.tryEmitNext("#" + nextNumber + ": "
                + new Faker().yoda().quote());
        if (result.isFailure()) {
            log.error("Fail to emit the quote: #" + nextNumber);
        }
    }
}
