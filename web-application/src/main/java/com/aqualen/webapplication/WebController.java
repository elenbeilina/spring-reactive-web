package com.aqualen.webapplication;

import com.github.javafaker.Faker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/quote")
public class WebController {

    @GetMapping
    public String getQuote(){
        return new Faker().yoda().quote();
    }

    @GetMapping("/delay")
    public String helloDelay() throws InterruptedException {
        Thread.sleep(new Random().nextInt(500, 1000));
        return new Faker().yoda().quote();
    }

    @GetMapping("/error")
    public ResponseEntity<String> error() {
        return ResponseEntity.badRequest()
                .body("Something awful happened!");
    }

}
