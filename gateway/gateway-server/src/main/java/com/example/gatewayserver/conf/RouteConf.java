package com.example.gatewayserver.conf;

import com.example.gatewayserver.filter.RateLimitFilterByCpu;
import com.example.gatewayserver.filter.RateLimitFilterByIp;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Configuration
public class RouteConf {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {

        ZonedDateTime dateTime = LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault());

        return builder
                .routes()
//                 .route("after_route", r -> r.after(dateTime).uri("http://baidu.com"))
                .route("header_route", r -> r.header("x-app-id", "baidu").uri("http://baidu.com"))
                .route(r -> r.path("/test/head")
                            .filters(f -> f.addRequestHeader("x-gateway-tag", "my-gateway-tag-v1"))
                        .uri("http://localhost:7001/test/head")
                        .id("add_header_route")
                )
                .route(r -> r.path("/test/rate_limit")
                                .filters(f -> f.filter(new RateLimitFilterByIp()))
                                        .uri("http://baidu.com")
                        .id("rate_limit_route")
                )
                .route(
                        r -> r.path("/test/cpu")
                                .filters(f -> f.filter(new RateLimitFilterByCpu()))
                                .uri("http://baidu.com")
                                .id("rate_limit_cpu_route")
                )
                .build();
    }

}
