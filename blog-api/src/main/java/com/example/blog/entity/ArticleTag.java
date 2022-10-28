package com.example.blog.entity;

import lombok.Data;

@Data
public class ArticleTag {
    private Long id;
    private Long articleId;
    private Long tagId;
}
