package com.example.gatewayserver.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilterByIp implements GatewayFilter {

    private final static Logger log = LoggerFactory.getLogger(RateLimitFilterByIp.class);

    private static final Map<String, Bucket> LOCAL_CACHE = new ConcurrentHashMap<>();

    private Bucket createBucket() {
        // 每间隔10 sec，填充1 token
        Refill refill = Refill.of(1, Duration.ofSeconds(10));

        // 带宽
        Bandwidth bandwidth = Bandwidth.classic(3, refill);

        return Bucket4j.builder().addLimit(bandwidth).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();

        Bucket bucket = LOCAL_CACHE.computeIfAbsent(ip, k -> createBucket());

        log.info("rate limit filter ip {}, available {}", ip, bucket.getAvailableTokens());

        if (bucket.tryConsume(1)) {
            return chain.filter(exchange);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }
    }
}
