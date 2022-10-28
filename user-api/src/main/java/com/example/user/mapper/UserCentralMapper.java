package com.example.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.common_utils.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserCentralMapper extends BaseMapper<User> {

}
