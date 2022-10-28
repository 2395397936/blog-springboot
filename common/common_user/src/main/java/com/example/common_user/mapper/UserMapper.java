package com.example.common_user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.common_utils.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
//@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<String> queryPermission(Long id);
}
