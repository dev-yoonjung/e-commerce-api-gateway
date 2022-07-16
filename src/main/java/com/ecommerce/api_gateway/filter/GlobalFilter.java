package com.ecommerce.api_gateway.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {


    public GlobalFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Custom pre-filter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Global filter base message: {}", config.getBaseMessage());

            if (config.isPreLogger()) {
                log.info("Global filter start: request id -> {}", request.getId());
            }

            // Custom post-filter
            return chain
                    .filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        if (config.isPostLogger()) {
                            log.info("Global filter end: response code -> {}", response.getStatusCode());
                        }
                    }));
        };
    }


    @Setter
    @Getter
    public static class Config {

        private String baseMessage;

        private boolean preLogger;

        private boolean postLogger;

    }

}
