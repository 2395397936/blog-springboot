package com.example.blog.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.blog.entity.Comment;
import com.example.blog.entity.Tag;
import com.example.blog.mapper.TagMapper;
import com.example.blog.vo.TagVo;
import com.example.common_utils.entity.R;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements com.example.blog.Service.TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> listTabById(Long id) {
        List<Tag> tags = tagMapper.findTagsByArticleId(id);
        return copyList(tags);
    }

    @Override
    public R getTag(long id) {
        return R.success(tagMapper.selectById(id));
    }

    @Override
    public List<TagVo> getAllTags() {
        List<Tag> tags = tagMapper.selectList(null);
        return copyList(tags);
    }

    @Override
    public void clear(Long id) {
        tagMapper.clear(id);
    }

    public TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }

    public List<TagVo> copyList(List<Tag> tagList) {
        List<TagVo> tagVoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagVoList.add(copy(tag));
        }
        return tagVoList;
    }

}
