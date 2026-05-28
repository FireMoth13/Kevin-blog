package com.kevin.blog.modules.article.service.impl;

import java.util.List;


import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevin.blog.modules.article.entity.Tag;
import com.kevin.blog.modules.article.mapper.TagMapper;
import com.kevin.blog.modules.article.service.TagService;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {


    @Override
    public List<Tag> listAll() {
        return list();
    }

    @Override
    public void add(Tag tag) {
        save(tag);
    }

    @Override
    public void update(Tag tag) {
        updateById(tag);
    }

    @Override
    public void delete(Long id) {
        removeById(id);
    }

    @Override
    public List<Tag> getByArticleId(Long articleId) {
        //需要用ArticleMapper的xml查询
        return baseMapper.selectList(new LambdaQueryWrapper<Tag>()
                .inSql(Tag::getId, 
                    "SELECT tag_id FROM blog_article_tag WHERE article_id = " + articleId));
    }
    
}
