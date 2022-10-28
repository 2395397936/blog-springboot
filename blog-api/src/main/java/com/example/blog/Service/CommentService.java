package com.example.blog.Service;


import com.example.common_utils.entity.R;
import com.example.blog.vo.params.CommentParam;

public interface CommentService {

    R getCommentsById(long id);

    R comment(CommentParam commentParam);

    void clear(Long id);
}
