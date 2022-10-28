package com.example.blog.Service;

import com.example.blog.entity.Article;
import com.example.blog.mapper.ArticleMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ThreadService {
    @Async("taskExecutor")
    public void addArticleViewCount(ArticleMapper articleMapper, Article article) {
        int viewCounts = article.getViewCounts();
        article.setViewCounts(viewCounts+1);
        articleMapper.updateById(article);
    }
}
