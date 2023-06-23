package com.example.providerserver.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @HystrixCommand(fallbackMethod = "getUserCallback")
    public String getUser(String username) throws Exception {
        if("test".equals(username)) {
            return "downstream invoke success";
        } else {
            throw new Exception("downstream invoke fail");
        }
    }

    public String getUserCallback(String username) {
        return "user callback return";
    }

}
