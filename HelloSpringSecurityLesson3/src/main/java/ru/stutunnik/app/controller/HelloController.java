package ru.stutunnik.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/greet/")
public class HelloController {

    @RequestMapping("/hi")
    public String sayHi() {
        return "Hi <^()^> admin!!!";
    }

    @RequestMapping("/hello")
    public String sayHello() {
        return "Hello <^=^> user!!!";
    }
}
