package com.example.gateway.handler;

import com.example.common_utils.entity.R;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.example.gateway.config.SecurityConfig.writeWith;

/**
 * 登录验证失败处理器
 */
@Component
public class AuthFailureHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
//        webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

        String message;
        if (exception instanceof UsernameNotFoundException) {
            message = "邮箱不存在";
        } else if (exception instanceof BadCredentialsException) {
            message = "密码错误 ";
        } else if (exception instanceof LockedException) {
            message = "用户锁定 ";
        } else if (exception instanceof AccountExpiredException) {
            message = "账户过期 ";
        } else if (exception instanceof DisabledException) {
            message = "账户不可用 ";
        } else{
            message = "系统错误 ";
        }
        return writeWith(webFilterExchange.getExchange(), R.fail(-200, message));
    }
}
