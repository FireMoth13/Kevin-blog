package com.kevin.blog.modules.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("blog_tag")
public class Tag {  //标签表
    @TableId(type=IdType.AUTO)
    private Long id;
    private String name;
    private String slug;  //URL友好名称
}
