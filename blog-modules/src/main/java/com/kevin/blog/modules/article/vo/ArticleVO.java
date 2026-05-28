package com.kevin.blog.modules.article.vo;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ArticleVO {
    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String cover;
    private String contentHtml;
    private String categoryName; //分类名称
    private List<TagBrief> tags; //标签列表
    private Integer viewCount;
    private Integer isTop;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private NavLink prev;
    private NavLink next;
}
