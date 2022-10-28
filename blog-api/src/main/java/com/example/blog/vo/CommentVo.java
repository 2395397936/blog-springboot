package com.example.blog.vo;


import com.example.common_utils.entity.UserVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class CommentVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private UserVo author;
    private String content;
    private List<CommentVo> children;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date createDate;
    private Integer level;
    private UserVo toUser;
}
