package com.kevin.blog.modules.article.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArticleSaveDTO {
    private Long id;  //文章ID，新增时为null，编辑时为对应文章ID
    
    @NotBlank(message = "文章标题不能为空")
    private String title;   

    @NotBlank(message = "文章内容不能为空")
    private String contentMd;  //Markdown格式内容

    private String summary;   //文章摘要
    private String cover;     //封面图片URL
    private Long categoryId;  //分类ID
    private Integer status;   //0:草稿,1:发布
    private Integer isTop;    //是否置顶
    private List<Long> tagIds;    //标签ID列表
}
