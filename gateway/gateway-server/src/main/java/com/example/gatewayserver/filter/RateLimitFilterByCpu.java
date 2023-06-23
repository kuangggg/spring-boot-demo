package com.example.gatewayserver.filter;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class RateLimitFilterByCpu implements GatewayFilter, Ordered {

    private final static Logger log = LoggerFactory.getLogger(RateLimitFilterByCpu.class);

    @Autowired
    private MetricsEndpoint metricsEndpoint;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Double cpuRate = metricsEndpoint.metric("system.cpu.usage", null)
                .getMeasurements()
                .stream()
                .filter(Objects::nonNull)
                .findFirst()
                .map(MetricsEndpoint.Sample::getValue)
                .filter(Double::isFinite)
                .orElse(0.0D);

        log.info("rate limit filter cpu {}", cpuRate);

        return chain.filter(exchange);
    }
}
