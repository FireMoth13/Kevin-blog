package com.kevin.blog.modules.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevin.blog.modules.article.entity.Article;
import com.kevin.blog.modules.article.dto.ArticleSaveDTO;
import com.kevin.blog.modules.article.dto.ArticleQueryDTO;
import com.kevin.blog.modules.article.vo.ArticleListVO;
import com.kevin.blog.modules.article.vo.ArticleVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

public interface ArticleService extends IService<Article> {
    //管理端
    void saveArticle(ArticleSaveDTO dto);
    void updateArticle(ArticleSaveDTO dto);
    void deleteArticle(Long id);

    //公开端
    IPage<ArticleListVO> pageArticles(ArticleQueryDTO dto);  //文章列表分页
    ArticleVO getDetailBySlug(String slug);  //文章详情
    List<ArticleListVO> getArchives();   //归档

    //浏览量+1
    void incrViewCount(Long id);

}
