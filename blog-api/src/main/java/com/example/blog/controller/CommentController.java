package com.example.blog.controller;


import com.example.common_utils.entity.R;
import com.example.common_utils.utils.JwtUtil;
import com.example.blog.Service.CommentService;
import com.example.blog.vo.params.CommentParam;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("/public/comments/{id}")
    @Cacheable(value = "comment",key = "#id")
    public R getCommentsById(@PathVariable long id){
        return commentService.getCommentsById(id);
    }

    @PostMapping("comment")
    @CacheEvict(value = "comment",key = "#commentParam.articleId")
    public R comment(@RequestBody CommentParam commentParam, HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Claims claims = JwtUtil.parseJWT(token);
        String userid = claims.getSubject();
        commentParam.setAuthorId(Long.parseLong(userid));
        return commentService.comment(commentParam);
    }
}
