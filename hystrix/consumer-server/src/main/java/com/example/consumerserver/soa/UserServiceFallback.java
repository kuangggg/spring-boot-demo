package com.example.consumerserver.soa;

import org.springframework.stereotype.Component;

@Component
public class UserServiceFallback implements IUserService{

    public String getUser(String username) {
        return "IUserService getUser callback ret";
    }
}
