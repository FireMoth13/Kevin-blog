package com.kevin.blog.modules.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("blog_category")
public class Category {   //分类表
    @TableId(type=IdType.AUTO)
    private Long id;
    private String name;
    private String slug;  //URL友好名称
    private String description;
    private Integer sort; //排序值，越小越靠前
}
