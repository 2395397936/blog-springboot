package com.example.gateway.handler;

import com.example.common_utils.entity.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.example.gateway.config.SecurityConfig.writeWith;


/**
 * 用来解决匿名用户访问无权限资源时的异常
 */
@Component
public class AuthEntryPoint extends HttpBasicServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange serverWebExchange, AuthenticationException e) {
        return writeWith(serverWebExchange, R.fail(401,"匿名用户访问无权限资源时的异常"));
    }
}

