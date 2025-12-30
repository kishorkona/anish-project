package com.anish.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class GreetingRestController {

    @GetMapping("/api/greet")
    public Map<String, Object> greet(@RequestParam(defaultValue = "World") String name) {
        return Map.of(
                "message", "Hello, " + name + "!",
                "timestamp", Instant.now().toString()
        );
    }
}

