package com.shermatov;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {

    private static int COUNTER = 0;

    @GetMapping("/ping")
    public PingPong getPingPong(){
        return new PingPong("Pong1: " + ++COUNTER);
    }

}
