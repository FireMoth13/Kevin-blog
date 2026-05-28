package com.kevin.blog.modules.article.dto;

import java.util.List;

import lombok.Data;

@Data
public class ArticleQueryDTO {
    private String keyword;  //搜索关键词，匹配标题或内容
    private Long categoryId; //按分类过滤
    private Long tagId;      //按标签过滤
    private Long pageNum = 1L;   //页码，默认1
    private Long limit = 10L; //每页条数，默认10

    private List<Long> articleIds; //批量查询文章ID列表
}
