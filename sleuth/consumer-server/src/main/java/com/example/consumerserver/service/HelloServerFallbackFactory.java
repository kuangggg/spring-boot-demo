package com.example.consumerserver.service;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class HelloServerFallbackFactory implements FallbackFactory<HelloService> {

    public HelloService create(Throwable throwable) {
        return new HelloService() {
            public String sayHello(String name) {
                return "sayHello fallback";
            }
        };
    }
}
