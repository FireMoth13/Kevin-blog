package com.kevin.blog.modules.article.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevin.blog.modules.article.entity.Category;
import com.kevin.blog.modules.article.mapper.CategoryMapper;
import com.kevin.blog.modules.article.service.CategoryService;
import com.kevin.blog.modules.article.entity.Article;
import com.kevin.blog.modules.article.mapper.ArticleMapper;
import com.kevin.blog.common.exception.BusinessException;
import com.kevin.blog.common.result.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleMapper articleMapper;
    
    @Override
    public List<Category> listAll() {
        return lambdaQuery().orderByAsc(Category::getSort).list();
    }

    @Override
    public Category getBySlug(String slug) {
        return lambdaQuery().eq(Category::getSlug, slug).one();
    }

    @Override
    public void add(Category category) {
        save(category);
    }

    @Override
    public void update(Category category) {
        updateById(category);
    }

    @Override
    public void delete(Long id) {
        //检查分类下是否有文章
        long count=articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getCategoryId,id));
        if(count>0){
            throw new BusinessException(ResultCode.BAD_REQUEST,"无法删除分类，请先删除该分类下的文章");
        }
        removeById(id);
    }
}
