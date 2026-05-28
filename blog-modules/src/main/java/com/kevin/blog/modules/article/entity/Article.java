package com.kevin.blog.modules.article.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("blog_article")
public class Article {
    @TableId(type=IdType.AUTO)
    private Long id;
    private String title;
    private String slug;  //URL友好标题
    private String summary;
    private String contentMd;
    private String contentHtml;
    private String cover;
    private Long categoryId;//分类ID
    private Integer status;  //0:草稿,1:发布
    private Integer viewCount;
    private Integer isTop;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
