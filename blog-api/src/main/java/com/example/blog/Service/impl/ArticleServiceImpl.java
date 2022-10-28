package com.example.blog.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.blog.Service.*;
import com.example.blog.dto.CategoryDto;
import com.example.blog.dto.TagDto;
import com.example.common_user.service.UserService;
import com.example.common_utils.entity.Errors;
import com.example.common_utils.entity.R;
import com.example.blog.entity.*;
import com.example.blog.mapper.ArticleBodyMapper;
import com.example.blog.mapper.ArticleMapper;
import com.example.blog.mapper.ArticleTagMapper;
import com.example.blog.vo.ArticleBodyVo;
import com.example.blog.vo.ArticleVo;
import com.example.blog.vo.CategoryVo;
import com.example.blog.vo.params.ArticleParam;
import com.example.blog.vo.params.PageParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private CommentService commentService;

    //    文章分页
    @Override
    public R listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("weight", "create_date");
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
        List<ArticleVo> articleVoList = copyList(records, true, true);
        System.out.println(records);
        return R.success(articleVoList);
    }

    //获取数量
    @Override
    public R getArticleCounts() {
        return R.success(articleMapper.selectCount(null));
    }

    //得到最新
    @Override
    public R getNewArticles() {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "title");
        queryWrapper.orderByDesc("create_date");
        queryWrapper.last("limit " + 5);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return R.success(copyList(articles, false, false));
    }

    //得到归档数量
    @Override
    public R getArchives() {
        List<Archive> archiveList = articleMapper.getArchives();
        return R.success(archiveList);
    }

    //得到年月文章
    @Override
    public R getByYearAndMonth(Archive archive) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        if (archive.getMonth() <= 9) {
            queryWrapper.like("create_date", archive.getYear() + "-0" + archive.getMonth());
        } else {
            queryWrapper.like("create_date", archive.getYear() + "-" + archive.getMonth());

        }
        List<Article> articles = articleMapper.selectList(queryWrapper);
        System.out.println(articles);
        return R.success(copyList(articles, true, true));
    }

    //找到具体文章
    @Override
    public R findArticleById(long id) {
        Article article = articleMapper.selectById(id);
        ArticleVo articleVo = copy(article, true, true, true, true);
        threadService.addArticleViewCount(articleMapper, article);
        return R.success(articleVo);
    }

    @Override
    public R publish(ArticleParam articleParam) {
        System.out.println(articleParam);
        Article article = new Article();
        BeanUtils.copyProperties(articleParam, article);
        articleMapper.insert(article);
//        标签
        List<Tag> tags = articleParam.getTags();
        for (Tag tag : tags) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(article.getId());
            articleTag.setTagId(tag.getId());
            articleTagMapper.insert(articleTag);
        }
//        body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getContent());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        return R.success(null,article.getId().toString());
    }

    @Override
    public R deleteArticle(Long userId,Long id) {
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("author_id",userId);
        articleQueryWrapper.eq("id",id);
        int delete = articleMapper.delete(articleQueryWrapper);
        if (delete == 0) {
            return R.fail(Errors.NullPointerError);
        }

        QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
        articleTagQueryWrapper.eq("article_id",id);
        articleTagMapper.delete(articleTagQueryWrapper);

        QueryWrapper<ArticleBody> articleBodyQueryWrapper = new QueryWrapper<>();
        articleBodyQueryWrapper.eq("article_id",id);
        articleBodyMapper.delete(articleBodyQueryWrapper);
        commentService.clear(id);
        tagService.clear(id);
        return R.success(null,"删除成功!");
    }

    @Override
    public R getByTag(PageParams pageParams,Long id) {
        List<Article> articles = articleMapper.getByTag(id,(pageParams.getPage()-1)*pageParams.getPageSize(),pageParams.getPageSize());
        System.out.println(articles);
        return R.success(copyList(articles, true, true));
    }

    @Override
    public R getByCategory(PageParams pageParams,Long id) {
        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id",id);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
        List<ArticleVo> articleVoList = copyList(records, true, true);
        System.out.println(records);
        return R.success(articleVoList);
    }

    @Override
    public R getSelfArticle(String userid) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("author_id",userid);
        queryWrapper.orderByDesc("create_date");
        return R.success(copyList(articleMapper.selectList(queryWrapper),false,false));
    }

    @Override
    public R update(ArticleParam articleParam,Long id) {
        Article article = new Article();
        BeanUtils.copyProperties(articleParam, article);
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.eq("author_id",articleParam.getAuthorId());
        articleQueryWrapper.eq("id",id);
        int update = articleMapper.update(article, articleQueryWrapper);
        if (update != 1) {
            return R.fail(Errors.NullPointerError);
        }
//        标签
        tagService.clear(id);
        List<Tag> tags = articleParam.getTags();
        for (int i = 0; i < tags.size(); i++) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(id);
            articleTag.setTagId(tags.get(i).getId());
            articleTagMapper.insert(articleTag);
        }
//        body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(id);
        articleBody.setContent(articleParam.getContent());
        QueryWrapper<ArticleBody> articleBodyQueryWrapper = new QueryWrapper<>();
        articleBodyQueryWrapper.eq("article_id",id);
        articleBodyMapper.update(articleBody,articleBodyQueryWrapper);

        return R.success(null,"修改成功");
    }

    @Override
    public R searchArticle(String name,PageParams pageParams) {
        List<Article> records = articleMapper.searchArticle(name,(pageParams.getPage()-1)*pageParams.getPageSize(),pageParams.getPageSize());
        List<ArticleVo> articleVoList = copyList(records, true, true);
        return R.success(articleVoList);
    }

    //向vo传递数据
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article article : records) {
            articleVoList.add(copy(article, isTag, isAuthor));
        }
        return articleVoList;
    }

    //向vo传递数据
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(article.getCreateDate());
        if (isTag) {
            Long id = article.getId();
            articleVo.setTags(tagService.listTabById(id));
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(userService.findUserById(authorId).getNickname());
        }
        return articleVo;
    }

    //向vo传递数据
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isCategory, boolean isArticleBody) {
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article, articleVo);
        articleVo.setCreateDate(article.getCreateDate());
        if (isTag) {
            Long id = article.getId();
            articleVo.setTags(tagService.listTabById(id));
        }
        if (isAuthor) {
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(userService.findUserById(authorId).getNickname());
        }
        if (isArticleBody) {
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory) {
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(findCategoryById(categoryId));
        }
        return articleVo;
    }

    private CategoryVo findCategoryById(Long categoryId) {
        return categoryService.findCategoryById(categoryId);
    }

    private ArticleBodyVo findArticleBodyById(Long id) {
        ArticleBody articleBody = articleBodyMapper.selectById(id);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }


}
