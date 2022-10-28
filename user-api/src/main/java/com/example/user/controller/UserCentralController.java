package com.example.user.controller;

import com.example.common_utils.entity.Errors;
import com.example.common_utils.entity.R;
import com.example.common_utils.entity.User;
import com.example.common_utils.entity.UserVo;
import com.example.common_utils.utils.JwtUtil;
import com.example.user.dto.AvatarDto;
import com.example.user.dto.UserDto;
import com.example.user.entity.PasswordDto;
import com.example.user.service.UserCentralService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserCentralController {
    @Autowired
    UserCentralService userCentralService;

    @DeleteMapping("deleteUser/{id}")
    public R deleteUser(@PathVariable Long id) {
        return userCentralService.deleteUser(id);
    }

    @PostMapping("register")
    public R register(@RequestBody UserDto userDto){
        return userCentralService.register(userDto);
    }

    @PostMapping("public/registerAdmin")
    public R registerAdmin(@RequestBody UserDto userDto){
        if (!userDto.getInviteCode().equals("abc159"))
            return R.fail(Errors.InviteCodeError);
        return userCentralService.registerAdmin(userDto);
    }

    @PostMapping("/public/forget")
    public R forget(@RequestBody UserDto userDto){
        return userCentralService.forget(userDto);
    }

    @PostMapping("modify")
    public R modify(@RequestBody UserVo userVo,@RequestHeader String token){
        System.out.println(token);
        try {
            return userCentralService.modify(userVo,token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("modifyPassword")
    public R modifyPassword(@RequestBody PasswordDto passwordDto, @RequestHeader String token){
        try {
            return userCentralService.modifyPassword(passwordDto,token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("modifyAvatar")
    public R modifyAvatar(@RequestBody AvatarDto avatarDto, @RequestHeader String token){
        try {
            return userCentralService.modifyAvatar(avatarDto,token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
