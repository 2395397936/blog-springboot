package com.example.gateway.handler;

import com.example.common_utils.entity.R;
import com.example.common_utils.utils.RedisCache;
import com.example.gateway.entity.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.example.gateway.config.SecurityConfig.writeWith;


/**
 * 退出登录处理器
 */
@Component
public class LogoutHandler implements ServerLogoutSuccessHandler {
    @Autowired
    RedisCache redisCache;
    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        // todo 清除redis数据
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        String userId = userDetail.getUser().getId().toString();
        System.out.println(userId);
        redisCache.deleteObject("login:"+userId);
        return writeWith(exchange.getExchange(), R.success(null,"退出成功"));
    }
}
