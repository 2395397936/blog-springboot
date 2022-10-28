package com.example.gateway.handler;


import com.example.common_utils.entity.R;
import com.example.common_utils.entity.UserVo;
import com.example.common_utils.utils.JwtUtil;
import com.example.common_utils.utils.RedisCache;
import com.example.gateway.entity.UserDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

import static com.example.gateway.config.SecurityConfig.writeWith;


/**
 * 验证成功后处理器
 */
@Component
public class AuthSuccessHandler implements ServerAuthenticationSuccessHandler {
@Autowired
RedisCache redisCache;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        String userId = userDetail.getUser().getId().toString();
        String token = JwtUtil.createJWT(userId);
        redisCache.setCacheObject("login:" + userId, userDetail,3,TimeUnit.HOURS);
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userDetail.getUser(),userVo);
        System.out.println(userVo);
        return writeWith(webFilterExchange.getExchange(), R.success(userVo,token));
    }
}
