package com.example.common_user.service;


import com.example.common_utils.entity.R;
import com.example.common_utils.entity.User;
import com.example.common_utils.entity.UserVo;

public interface UserService {
    User findUserById(Long authorId);
    UserVo findUserVoById(Long authorId);
}
