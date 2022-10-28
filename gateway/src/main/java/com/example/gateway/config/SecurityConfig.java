package com.example.gateway.config;

import cn.hutool.json.JSONUtil;
import com.example.common_utils.entity.R;
import com.example.gateway.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 登录配置
 */
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final String[] permitAll = {"/user/public/**","/login", "/blog/public/**", "/user/register","/email/send" };

    /**
     * 自定义filterChain
     */
    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http,
                                              AuthEntryPoint authEntryPoint,
                                              AccessDeniedHandler accessDeniedHandler,
                                              AuthSuccessHandler authSuccessHandler,
                                              AuthFailureHandler authFailureHandler,
                                              LogoutHandler logoutHandler,
                                              CheckTokenFilter checkTokenFilter,
                                              JwtSecurityContextRepository jwtSecurityContextRepository,
                                              ReactiveAuthenticationManager reactiveAuthenticationManager) {
        http.csrf().disable()
                .httpBasic().disable()
                .authenticationManager(reactiveAuthenticationManager)
                .exceptionHandling()
//                权限不足
                .authenticationEntryPoint(authEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .accessDeniedHandler((swe,e)->{
                    swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return swe.getResponse().writeWith(Mono.just(new DefaultDataBufferFactory().wrap("forbidden".getBytes())));
                })
                .and()
// 配置上下文验证器(鉴权管理器)
                .securityContextRepository(jwtSecurityContextRepository)
//                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange()
                .pathMatchers(permitAll).permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .formLogin().loginPage("/login")
//                认证成功
                .authenticationSuccessHandler(authSuccessHandler)
//                认证失败
                .authenticationFailureHandler(authFailureHandler)
                .and()
                .logout().logoutUrl("/logout")
//                注销成功
                .logoutSuccessHandler(logoutHandler)
                .and()
                //                自定义过滤
                .addFilterAt(checkTokenFilter, SecurityWebFiltersOrder.HTTP_BASIC);
        return http.build();
    }

    /**
     * 提供用于获取UserDetail的Service
     */
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(
            ReactiveUserDetailsService userDetailsService,
            PasswordEncoder encoder) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(encoder);
        return authenticationManager;
    }

    /**
     * 输出响应信息
     */
    public static Mono<Void> writeWith(ServerWebExchange exchange, R responseMap) {
        ServerHttpResponse response = exchange.getResponse();
        String body = JSONUtil.toJsonPrettyStr(responseMap);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        //指定编码，否则在浏览器中会中文乱码?测试一下再加
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        // response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 注入加密方式，替换原来的数据库查询加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

