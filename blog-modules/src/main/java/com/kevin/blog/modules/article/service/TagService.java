package com.kevin.blog.modules.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevin.blog.modules.article.entity.Tag;
import java.util.List;

public interface TagService extends IService<Tag> {
    List<Tag> listAll();
    void add(Tag tag);
    void update(Tag tag);
    void delete(Long id);
    List<Tag> getByArticleId(Long articleId);  //查某篇文章的标签
    
}
