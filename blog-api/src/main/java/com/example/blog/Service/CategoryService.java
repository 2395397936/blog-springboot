package com.example.blog.Service;


import com.example.blog.entity.Category;
import com.example.blog.entity.Tag;
import com.example.common_utils.entity.R;
import com.example.blog.vo.CategoryVo;

public interface CategoryService {

    CategoryVo findCategoryById(Long categoryId);

    R getCategories();

    R getCategory(long parseLong);
}
