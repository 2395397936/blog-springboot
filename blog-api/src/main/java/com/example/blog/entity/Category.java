package com.example.blog.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class Category {
    @JsonSerialize(using = ToStringSerializer.class)
    private long id;
    private String avatar;
    private String categoryName;
}
