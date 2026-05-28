package com.kevin.blog.modules.article.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("blog_article_tag")  //文章标签关联表
public class ArticleTag {
    private Long articleId;
    private Long tagId;
}
