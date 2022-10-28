package com.example.user.service.imp;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common_user.mapper.UserMapper;
import com.example.common_utils.entity.Errors;
import com.example.common_utils.entity.R;

import com.example.common_utils.entity.User;
import com.example.common_utils.entity.UserVo;
import com.example.common_utils.utils.JwtUtil;
import com.example.common_utils.utils.RedisCache;
import com.example.user.dto.AvatarDto;
import com.example.user.dto.UserDto;
import com.example.user.entity.PasswordDto;
import com.example.user.mapper.UserCentralMapper;
import com.example.user.service.UserCentralService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class UserCentralCentralServiceImpl implements UserCentralService {
    @Autowired
    private UserCentralMapper userCentralMapper;
     @Autowired
    UserMapper userMapper;
    @Autowired
    RedisCache redisCache;
    @Autowired
    PasswordEncoder encoder;


    @Override
    public R deleteUser(Long id) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", id);
        int delete = userCentralMapper.delete(userQueryWrapper);
        if (delete == 1) {
            return R.success(null, "删除成功");
        } else {
            return R.fail(Errors.NoneUserError.getCode(), Errors.NoneUserError.getMsg());
        }
    }

    @Override
    public R register(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",userDto.getEmail());
        if (userMapper.selectOne(queryWrapper) != null) {
            return R.fail(Errors.ExistError);
        }
        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        if (redisCache.getCacheObject(user.getEmail())==null){
            return R.fail(Errors.RegisterExpireEmailError);
        }
        if (!(userDto.getCode().equals(redisCache.getCacheObject(user.getEmail()).toString()))){
            System.out.println( redisCache.getCacheObject(user.getEmail()).toString());
            return R.fail(Errors.RegisterCodeError);
        }
        String emailMatcher="[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+";

        boolean isMatch = Pattern.matches(emailMatcher,user.getEmail());
        if (!isMatch){
            return R.fail(Errors.RegisterEmailError);
        }
        if (user.getPassword().length()>11||user.getPassword().length()<6){
            return R.fail(Errors.PasswordError);
        }
        if (user.getNickname().length()>11||user.getNickname().length()<2){
            return R.fail(Errors.RegisterNicknameError);
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAvatar("https://blog-pys.oss-cn-hangzhou.aliyuncs.com/5bd50bd6c5e245aa86506c138cea6993.png");
        userCentralMapper.insert(user);
        return R.success("success","注册成功！");
    }
    @Override
    public R registerAdmin(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",userDto.getEmail());
        if (userMapper.selectOne(queryWrapper) != null) {
            return R.fail(Errors.ExistError);
        }
        User user = new User();
        BeanUtils.copyProperties(userDto,user);

        String emailMatcher="[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+";

        boolean isMatch = Pattern.matches(emailMatcher,user.getEmail());
        if (!isMatch){
            return R.fail(Errors.RegisterEmailError);
        }
        if (user.getPassword().length()>11||user.getPassword().length()<6){
            return R.fail(Errors.PasswordError);
        }
        if (user.getNickname().length()>11||user.getNickname().length()<2){
            return R.fail(Errors.RegisterNicknameError);
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAvatar("https://blog-pys.oss-cn-hangzhou.aliyuncs.com/5bd50bd6c5e245aa86506c138cea6993.png");
        user.setAdmin(true);
        userCentralMapper.insert(user);
        return R.success("success","注册成功！");
    }
    @Override
    public R modify(UserVo userVo,String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String id = claims.getSubject();
        User user = userMapper.selectById(id);
        if (user == null) {
            return R.fail(Errors.NullPointerError);
        }
        if (!Objects.equals(user.getEmail(), userVo.getEmail())) {
            return R.fail(Errors.AccessDeniedError);
        }
        if (user.getNickname().length()>11||user.getNickname().length()<2){
            return R.fail(Errors.RegisterNicknameError);
        }
        User userModify = new User();
        if (user.getPassword().length()>11||user.getPassword().length()<6){
            return R.fail(Errors.PasswordError);
        }
        BeanUtils.copyProperties(userVo,userModify);
        userModify.setId(Long.valueOf(id));
        userMapper.updateById(userModify);
        return R.success(null,"修改成功");
    }

    @Override
    public R modifyPassword(PasswordDto passwordDto, String token) throws Exception {
        if (passwordDto.getPassword().length()>11||passwordDto.getPassword().length()<6){
            return R.fail(Errors.PasswordError);
        }
        Claims claims = JwtUtil.parseJWT(token);
        String id = claims.getSubject();
        User userModify = new User();
        userModify.setId(Long.valueOf(id));
        userModify.setPassword(encoder.encode(passwordDto.getPassword()));
        userMapper.updateById(userModify);
        return R.success(null,"密码修改成功");
    }

    @Override
    public R modifyAvatar(AvatarDto avatarDto, String token) throws Exception {
        String id = JwtUtil.parseJWT(token).getSubject();
        User user = new User();
        user.setId(Long.valueOf(id));
        user.setAvatar(avatarDto.getAvatar());
        userMapper.updateById(user);
        return R.success("null","头像修改成功");
    }

    @Override
    public R forget(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",userDto.getEmail());
        if (userMapper.selectOne(queryWrapper) == null) {
            return R.fail(Errors.NullPointerError);
        }
        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        if (redisCache.getCacheObject(user.getEmail())==null){
            return R.fail(Errors.RegisterExpireEmailError);
        }
        if (!(userDto.getCode().equals(redisCache.getCacheObject(user.getEmail()).toString()))){
            System.out.println( redisCache.getCacheObject(user.getEmail()).toString());
            return R.fail(Errors.RegisterCodeError);
        }
        if (user.getPassword().length()>11||user.getPassword().length()<6){
            return R.fail(Errors.PasswordError);
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userCentralMapper.update(user,queryWrapper);
        return R.success("success","修改成功！");
    }



}
