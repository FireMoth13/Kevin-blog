package com.kevin.blog.modules.article.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kevin.blog.modules.article.entity.Category;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    
}
