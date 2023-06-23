package com.example.providerserver.controller;

import com.example.providerserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUser")
    public String getUser(@RequestParam(name = "username", defaultValue = "default username") String name) throws Exception {
        return userService.getUser(name);
    }
}
