package com.example.blog.Service.impl;


import com.example.common_utils.entity.R;
import com.example.blog.Service.CategoryService;
import com.example.blog.entity.Category;
import com.example.blog.mapper.CategoryMapper;
import com.example.blog.vo.CategoryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        categoryVo.setId(category.getId());
        categoryVo.setCategoryName(category.getCategoryName());
        return categoryVo;
    }

    @Override
    public R getCategories() {
        return R.success(categoryMapper.selectList(null));
    }

    @Override
    public R getCategory(long id) {
        return R.success(categoryMapper.selectById(id));
    }

}
