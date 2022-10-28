package com.example.gateway.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common_utils.entity.User;
import com.example.gateway.entity.UserDetail;
import com.example.gateway.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 用户认证与权限查询
 * <p>用于替换默认的内存中认证</p>
 */
@Component
public class UserService implements ReactiveUserDetailsService {
    @Autowired
    UserMapper userMapper;

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        System.out.println("----------"+email);
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("email",email);
        User user = userMapper.selectOne(queryWrapper);
        //TODO 权限查询
        if (user == null) {
            throw new UsernameNotFoundException("邮箱不存在");
        }
        //  查询权限
        List<String> permissions = userMapper.queryPermission(user.getId());
        UserDetail userDetail = new UserDetail();
        userDetail.setUser(user);
        userDetail.setPermissions(permissions);

        return Mono.just(userDetail);
    }
}
