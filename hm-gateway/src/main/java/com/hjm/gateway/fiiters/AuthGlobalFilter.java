package com.hjm.gateway.fiiters;


import com.hjm.gateway.config.AuthProperties;
import com.hjm.gateway.utils.JwtTool;
import com.hmall.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthGlobalFilter implements GlobalFilter , Ordered {

    private final AuthProperties authProperties;

    private final JwtTool jwtTool;

    private final AntPathMatcher antPathMatcher;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取request
        ServerHttpRequest request = exchange.getRequest();
        // 2.判断是否需要做登录拦截
        if (isExclude(request.getURI().getPath().toString())){
            // 放行
            return chain.filter(exchange);
        }
        // 3.获取token
        HttpHeaders headers = request.getHeaders();
        String token =null;
        List<String> strings = headers.get("authorization");
        if (strings != null && !strings.isEmpty()){
            token = strings.get(0);
        }
        Long userId = null;
        // 4.解析token
        try {
            userId = jwtTool.parseToken(token);
        } catch (UnauthorizedException e) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.valueOf(401));
            return response.setComplete();
        }
        // 5.传递用户信息
        String userInfo = userId.toString();
        ServerWebExchange serverWebExchange = exchange.mutate()
                .request(builder -> builder.header("user-info", userInfo))
                .build();
        // 6.放行
        return chain.filter(serverWebExchange);
    }

    private boolean isExclude(String path) {
        return authProperties.getExcludePaths()
                .stream()
                .anyMatch(p -> antPathMatcher.match(p, path));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
