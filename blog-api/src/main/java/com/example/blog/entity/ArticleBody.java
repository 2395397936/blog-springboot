package com.example.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "article_body")
public class ArticleBody {

    @TableId(value = "id",type = IdType.AUTO)
    private long id;
    private String content;
    private String content_html;
    private long articleId;
}
