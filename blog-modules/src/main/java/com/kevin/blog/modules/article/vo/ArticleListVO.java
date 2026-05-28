package com.kevin.blog.modules.article.vo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ArticleListVO {
    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String cover;
    private String categoryName; //分类名称
    private Integer viewCount;
    private Integer isTop;
    private LocalDateTime createTime;
    private List<TagBrief> tags; //标签列表
}
