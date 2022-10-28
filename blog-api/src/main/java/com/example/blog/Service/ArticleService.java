package com.example.blog.Service;

import com.example.blog.dto.CategoryDto;
import com.example.blog.dto.TagDto;
import com.example.common_utils.entity.R;
import com.example.blog.entity.Archive;
import com.example.blog.vo.params.ArticleParam;
import com.example.blog.vo.params.PageParams;

public interface ArticleService {
    R listArticle(PageParams page);
    R getArticleCounts();

    R getNewArticles();

    R getArchives();

    R getByYearAndMonth(Archive archive);

    R findArticleById(long id);

    R publish(ArticleParam articleParam);

    R deleteArticle(Long userId,Long id);

    R getByTag(PageParams page,Long id);

    R getByCategory(PageParams page,Long id);

    R getSelfArticle(String userid);

    R update(ArticleParam articleParam,Long id);

    R searchArticle(String name,PageParams pageParams);
}
