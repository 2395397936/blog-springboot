package com.example.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blog.entity.Archive;
import com.example.blog.entity.Article;
import com.example.blog.vo.params.PageParams;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

    List<Archive> getArchives();
    List<Article> getByTag(Long id,int pageStart,int pageSize);
    List<Article> searchArticle(String name,int pageStart,int pageSize);
}
