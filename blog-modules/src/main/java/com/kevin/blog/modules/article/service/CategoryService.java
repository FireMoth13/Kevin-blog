package com.kevin.blog.modules.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevin.blog.modules.article.entity.Category;
import java.util.List;

public interface CategoryService extends IService<Category> {
    List<Category> listAll();
    Category getBySlug(String slug);
    void add(Category category);
    void update(Category category);
    void delete(Long id);
}
