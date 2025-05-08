package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MatchOddsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchOddsApplication.class, args);
    }
}
