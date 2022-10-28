package com.example.blog.Service;

import com.example.blog.entity.Tag;
import com.example.blog.vo.TagVo;
import com.example.common_utils.entity.R;

import java.util.List;

public interface TagService {
    List<TagVo> listTabById(Long id);

    R getTag(long id);

    List<TagVo> getAllTags();

    void clear(Long id);
}
