package com.example.downsteramserver.controller;

import ch.qos.logback.core.util.TimeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
public class TestController {

    @GetMapping("/test/head")
    public String testHead(@RequestHeader("x-gateway-tag") String tag) {
        return "gateway downstream server return tag :" + tag;
    }

    @GetMapping("/test/hystrix")
    public String testHystrix() {
        Random random = new Random();
        int i = random.nextInt(10);

        try {
            TimeUnit.SECONDS.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "gateway downstream success cost :" + i + " sec";
    }
}