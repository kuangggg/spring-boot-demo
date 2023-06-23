package com.example.consumerserver.controller;

import com.example.consumerserver.soa.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private IUserService iUserService;

    @GetMapping("/getUser")
    public String getUser(@RequestParam(name = "username", defaultValue = "default username") String name) throws Exception {
        return iUserService.getUser(name);
    }
}
