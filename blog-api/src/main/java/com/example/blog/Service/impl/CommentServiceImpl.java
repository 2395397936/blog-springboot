package com.example.blog.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.blog.entity.Article;
import com.example.blog.mapper.ArticleMapper;
import com.example.common_user.service.UserService;
import com.example.common_utils.entity.R;
import com.example.common_utils.entity.UserVo;
import com.example.blog.Service.CommentService;
import com.example.blog.entity.Comment;
import com.example.blog.mapper.CommentMapper;
import com.example.blog.vo.CommentVo;
import com.example.blog.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserService userService;
    @Autowired
    ArticleMapper articleMapper;

    @Override
    public R getCommentsById(long id) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", id);
        queryWrapper.eq("level", 1);
        queryWrapper.orderByDesc("create_date");
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        return R.success(copyList(comments));
    }

    @Override
    public R comment(CommentParam commentParam) {
        if (Objects.equals(commentParam.getContent(), "")){
            return R.fail(-200,"请输入内容");
        }

        long parentId = commentParam.getParentId();
        long articleId = commentParam.getArticleId();
        addCommentCount(articleId);

        Comment comment = commentMapper.selectById(parentId);
        Comment commentInsert = new Comment();
        BeanUtils.copyProperties(commentParam, commentInsert);
        if (comment != null) {
            if (comment.getLevel() == 3) {
                commentInsert.setLevel(3);
                commentInsert.setParentId(comment.getParentId());
            } else {
                commentInsert.setLevel(comment.getLevel() + 1);
            }
        }else {
            commentInsert.setLevel(1);
        }
        commentMapper.insert(commentInsert);
        return R.success(null,"评论成功");

    }
    @Async("taskExecutor")
    void addCommentCount(long articleId) {
        Article article = articleMapper.selectById(articleId);
        article.setCommentCounts(article.getCommentCounts()+1);
        articleMapper.updateById(article);
    }

    @Override
    public void clear(Long id) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id",id);
        commentMapper.delete(queryWrapper);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        Long authorId = comment.getAuthorId();
        UserVo userVo = userService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        Integer level = comment.getLevel();
        if (level == 1) {
            Long id = comment.getId();
            List<CommentVo> commentVoList = searchChildren(id, level);
            commentVo.setChildren(commentVoList);
        }
        if (level == 2) {
//            Long toUid = comment.getToUid();
//            UserVo userVoById = userCentralService.findUserVoById(toUid);
//            commentVo.setToUser(userVoById);
            Long id = comment.getId();
            List<CommentVo> commentVoList = searchChildren(id, level);
            commentVo.setChildren(commentVoList);
        }
        if (level == 3) {
            Long toUid = comment.getToUid();
            UserVo userVoById = userService.findUserVoById(toUid);
            commentVo.setToUser(userVoById);
        }
        return commentVo;
    }

    private List<CommentVo> searchChildren(Long id, Integer level) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level", level + 1);
        queryWrapper.eq("parent_id", id);
        return copyList(commentMapper.selectList(queryWrapper));
    }
}
