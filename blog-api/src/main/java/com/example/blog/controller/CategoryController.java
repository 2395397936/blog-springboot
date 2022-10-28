package com.example.blog.controller;


import com.example.blog.dto.CategoryDto;
import com.example.common_utils.entity.R;
import com.example.blog.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping("public/getCategories")
    @Cacheable(value = "category",key = "'all'")
    public R getCategories(){
        return categoryService.getCategories();
    }

    //    拿到目录图标
    @PostMapping("public/getCategoryById/{id}")
    @Cacheable(value = "category",key = "'avatar'")
    public R getCategoryById(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }
}
