package com.example.providerserver.controller;

import brave.propagation.ExtraFieldPropagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/sayHello")
    public String sayHello(@RequestParam("name") String name) {
        log.info("request params: {} {}", name, ExtraFieldPropagation.get("trace_id"));
        return "hello " + name;
    }
}
