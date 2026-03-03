package com.ecommerce.gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter(){
        return new RedisRateLimiter(10,20,1);
    }

    @Bean
    public KeyResolver hostNameKeyResolver(){
     return exchange -> Mono.just
             (exchange.getRequest().getRemoteAddress().getHostName());
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("product-service",
                        x->x.path("/api/v1/products/**")
                                .filters(f->f.requestRateLimiter(config ->
                                                config.setRateLimiter(redisRateLimiter())
                                                        .setKeyResolver(hostNameKeyResolver()))
                                        .circuitBreaker
                                        (config->config.setName("ecomBreaker")
                                                .setFallbackUri("forward:/fallback/products")))
//                                .filters(a->a.rewritePath("/products(?<segment>/?.*)"
//                                        ,"/api/v1/product${segment}"))
                                .uri("http://localhost:8081"))


                .route("user-service",x->x.path("/api/v1/users/**")
                        .filters(u->u.circuitBreaker(us->us.setName("ecomBreaker")
                                .setFallbackUri("forward:/fallback/users")))
//                        .filters(s->s.rewritePath("/users(?<segment>/?.*)","/api/v1/users${segment}"))
                                .uri("http://localhost:8082"))


                .route("order-services",a->a.path("/api/v1/orders/**")
                        .filters(co->co.circuitBreaker(cc->cc.setName("ecomBreaker")
                                .setFallbackUri("forward:/fallback/orders")))
//                        .filters(c->c.rewritePath("/orders(?<segment>/?.*)","/api/v1/orders${segment}"))
                                .uri("http://localhost:8083"))


                .route("order-services-cart",b->b.path("/api/v1/cart/**")
                        .filters(ca->ca.circuitBreaker(cart->cart.setName("ecomBreaker")
                                .setFallbackUri("forward:/fallback/carts")))
//                        .filters(s->s.rewritePath("/cart(?<segment>/?.*)","/api/v1/cart${segment}"))
                        .uri("http://localhost:8083"))


                .route("eureka-server",a->a.path("/eureka/main")
                        .filters(f->f.rewritePath("/eureka/main","/"))
                        .uri("http://localhost:8761"))


                .route("eureka-server-static",a->a.path("/eureka/**")
                        .uri("http://localhost:8761"))
                .build();

    }
}
