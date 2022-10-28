package com.example.user.service;


import com.example.common_utils.entity.R;
import com.example.common_utils.entity.User;
import com.example.common_utils.entity.UserVo;
import com.example.user.dto.AvatarDto;
import com.example.user.dto.UserDto;
import com.example.user.entity.PasswordDto;

public interface UserCentralService {
    R deleteUser(Long id);

    R register(UserDto userDto);

    R modify(UserVo userVo,String token) throws Exception;

    R modifyPassword(PasswordDto passwordDto, String token) throws Exception;

    R modifyAvatar(AvatarDto avatarDto, String token) throws  Exception;

    R forget(UserDto userDto);

    R registerAdmin(UserDto userDto);
}
