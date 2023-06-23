package com.example.consumerserver.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "provider-server",
        url = "http://127.0.0.1:7001",
        fallbackFactory = HelloServerFallbackFactory.class
)
public interface HelloService {

    @GetMapping("/sayHello")
    String sayHello(@RequestParam("name") String name);
}
