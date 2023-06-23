package com.example.consumerserver.controller;

import com.example.consumerserver.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@RestController
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private HelloService helloService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExecutorService executorService;

    @GetMapping("/hello_by_feign")
    public String helloByFeign(String name) {
        String s = helloService.sayHello(name);
        log.info("fegin client received result : {}", s);
        return s;
    }

    @GetMapping("/hello_by_rest")
    public String helloByRest(String name) {
        String url = "http://localhost:7001/sayHello?name=" + name;
        String result = restTemplate.getForObject(url, String.class);
        log.info("rest template client received result : {}", result);
        return result;
    }

    @GetMapping("/hello_by_pool")
    public String helloByThreadPool() throws ExecutionException, InterruptedException {
        Future<String> future = executorService.submit(() -> {
            log.info("thread pool ->");
            return helloService.sayHello("pool");
        });

        String s = future.get();
        log.info("thread pool ret {}", s);
        return s;
    }

}
