package com.example.gateway.handler;

import com.example.common_utils.entity.R;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.example.gateway.config.SecurityConfig.writeWith;


/**
 * 用来解决认证过的用户访问无权限资源时的异常
 */
@Component
public class AccessDeniedHandler implements ServerAccessDeniedHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, AccessDeniedException e) {
        return writeWith(serverWebExchange, R.fail(401, "用户没有足够的权限"));
    }
}

