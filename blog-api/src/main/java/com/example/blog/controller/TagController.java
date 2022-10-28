package com.example.blog.controller;


import com.example.blog.dto.TagDto;
import com.example.common_utils.entity.R;
import com.example.blog.Service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TagController {
    @Autowired
    TagService tagService;

    @PostMapping("public/getAllTags")
    @Cacheable(value = "tag",key = "'all'")
    public R getAllTags(){
        return R.success(tagService.getAllTags());
    }

    //    拿到标签图标
    @PostMapping("public/getTagById/{id}")
    @Cacheable(value = "category",key = "'avatar'")
    public R getTagById(@PathVariable Long id) {
        return tagService.getTag(id);
    }
}
