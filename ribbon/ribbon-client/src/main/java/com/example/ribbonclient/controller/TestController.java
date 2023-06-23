package com.example.ribbonclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/ribbon-add")
    public String ribbonAdd(Integer a, Integer b) {
        String forObject = restTemplate.getForObject("http://provider-server/add?a=" + 1 + "&b=" + 2, String.class);
        System.out.println(forObject);
        return forObject;
    }

    @GetMapping("/add")
    public String add(Integer a, Integer b) {
        String forObject = restTemplate.getForObject("http://127.0.0.1:7070/add?a=" + 1 + "&b=" + 2, String.class);
        System.out.println(forObject);
        return forObject;
    }
}
