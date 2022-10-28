package com.example.blog.vo.params;

import com.example.blog.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleParam {
    Long authorId;
    List<Tag> tags;
    String title;
    String content;
    Long categoryId;
    String summary;
}
