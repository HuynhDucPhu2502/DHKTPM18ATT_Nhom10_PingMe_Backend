package me.huynhducphu.PingMe_Backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin 8/4/2025
 **/
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Hello world";
    }

    @GetMapping("/public")
    public String publicTest() {
        return "Hello world PUBLIC";
    }
}
