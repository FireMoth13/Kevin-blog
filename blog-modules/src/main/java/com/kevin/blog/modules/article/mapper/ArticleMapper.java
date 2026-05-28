package com.kevin.blog.modules.article.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kevin.blog.modules.article.dto.ArticleQueryDTO;
import com.kevin.blog.modules.article.entity.Article;
import com.kevin.blog.modules.article.vo.ArticleListVO;
import com.kevin.blog.modules.article.vo.ArticleVO;
import com.kevin.blog.modules.article.vo.NavLink;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    //自定义分页查询，包含分类和标签信息
    IPage<ArticleListVO> selectPageWithDetails(Page<ArticleListVO> page,@Param("dto")ArticleQueryDTO dto);
    
    //根据slug查询文章详情，包含分类和标签信息
    ArticleVO selectDetailBySlug(@Param("slug")String slug);

    //上一篇文章
    NavLink selectPrev(@Param("createTime")LocalDateTime createTime);

    //下一篇文章
    NavLink selectNext(@Param("createTime")LocalDateTime createTime);

    //按标签查文章ID集合
    List<Long> selectArticleIdsByTagId(@Param("tagId")Long tagId);
}
