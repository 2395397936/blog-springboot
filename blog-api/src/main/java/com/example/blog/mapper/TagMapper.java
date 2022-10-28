package com.example.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blog.entity.Tag;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {
    //根据文字id查标签
    List<Tag> findTagsByArticleId(Long id);

    void clear(Long id);
}
