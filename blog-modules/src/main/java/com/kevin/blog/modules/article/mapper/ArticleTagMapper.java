package com.kevin.blog.modules.article.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kevin.blog.modules.article.entity.ArticleTag;

@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
    
}
