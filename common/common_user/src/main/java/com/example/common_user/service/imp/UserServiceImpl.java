package com.example.common_user.service.imp;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common_user.mapper.UserMapper;
import com.example.common_user.service.UserService;
import com.example.common_utils.entity.Errors;
import com.example.common_utils.entity.R;
import com.example.common_utils.entity.User;
import com.example.common_utils.entity.UserVo;
import com.example.common_utils.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    RedisCache redisCache;

    @Override
    public User findUserById(Long authorId) {
        User user = userMapper.selectById(authorId);
        if (user == null) {
            user = new User();
            user.setNickname("佚名");
        }
        return user;
    }
    public UserVo findUserVoById(Long authorId) {
        User user = userMapper.selectById(authorId);
        UserVo userVo =new UserVo();
        if (user == null) {
            user = new User();
            user.setNickname("佚名");
        }
        BeanUtils.copyProperties(user,userVo);
        return userVo;
    }




    private UserVo copyUser(User user){
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        return userVo;
    }
}
