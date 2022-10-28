package com.example.blog.controller;

import com.example.blog.dto.CategoryDto;
import com.example.blog.dto.TagDto;
import com.example.common_utils.entity.R;
import com.example.common_utils.utils.JwtUtil;
import com.example.blog.Service.ArticleService;
import com.example.blog.entity.Archive;
import com.example.blog.vo.params.ArticleParam;
import com.example.blog.vo.params.PageParams;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ArticleController {
    @Autowired
    ArticleService articleService;

    //分页文章
    @PostMapping("public/getArticles")
    @Cacheable(value = "article", key = "'all:'+#page.page")
    public R listArticle(@RequestBody PageParams page) {
        return articleService.listArticle(page);
    }

    //文章总数
    @PostMapping("public/getArticleCounts")
    @Cacheable(value = "article", key = "'count'")
    public R getArticleCounts() {
        return articleService.getArticleCounts();
    }

    //最新5篇
    @PostMapping("public/getNewArticles")
    @Cacheable(value = "article", key = "'new'")
    public R getNewArticles() {
        return articleService.getNewArticles();
    }

    //归档
    @PostMapping("public/getArchives")
    @Cacheable(value = "article", key = "'archive'")
    public R getArchives() {
        return articleService.getArchives();
    }

    //按年月拿到文章
    @PostMapping("public/getByYearAndMonth")
    @Cacheable(value = "article", key = "'byTime'")
    public R getByYearAndMonth(@RequestBody Archive archive) {
        return articleService.getByYearAndMonth(archive);
    }

    //按标签拿到文章
    @PostMapping("public/getByTag/{id}")
    @Cacheable(value = "article", key = "'byTag'")
    public R getByYearAndMonth(@RequestBody PageParams page, @PathVariable Long id) {
        return articleService.getByTag(page, id);
    }

    //按分类拿到文章
    @PostMapping("public/getByCategory/{id}")
    @Cacheable(value = "article", key = "'byId'")
    public R getByCategory(@RequestBody PageParams page, @PathVariable Long id) {
        return articleService.getByCategory(page, id);
    }

    //拿到文章body
    @PostMapping("public/view/{id}")
    @Cacheable(value = "articleBody", key = "#id")
    public R findArticleById(@PathVariable long id) {
        return articleService.findArticleById(id);
    }

    //添加文章
    @PostMapping("publish")
    @CacheEvict(value = "article", allEntries = true)
    public R publish(@RequestBody ArticleParam articleParam, HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Claims claims = JwtUtil.parseJWT(token);
        String userid = claims.getSubject();
        articleParam.setAuthorId(Long.valueOf(userid));
        return articleService.publish(articleParam);
    }

    //修改文章
    @PostMapping("updateArticle/{id}")
    @CacheEvict(value = "articleBody", key = "#id")
    public R updateArticle(@RequestBody ArticleParam articleParam, @RequestHeader("token") String token, @PathVariable Long id) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userid = claims.getSubject();
        articleParam.setAuthorId(Long.valueOf(userid));
        return articleService.update(articleParam, id);
    }

    //    删除文章
    @PostMapping("deleteArticle/{id}")
    @CacheEvict(value = "article", allEntries = true)
    public R deleteArticle(@PathVariable Long id, @RequestHeader("token") String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userId = claims.getSubject();
        return articleService.deleteArticle(Long.valueOf(userId), id);
    }

    //    查看自己文章
    @PostMapping("getSelfArticle")
    public R getSelfArticle(@RequestHeader String token) throws Exception {
        Claims claims = JwtUtil.parseJWT(token);
        String userid = claims.getSubject();
        return articleService.getSelfArticle(userid);
    }

    //    搜索文章
    @PostMapping("public/searchArticle/{name}")
    public R searchArticle(@PathVariable String name,PageParams pageParams) {
        return articleService.searchArticle(name,pageParams);
    }
}
