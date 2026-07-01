package com.kevin.blog.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.blog.common.exception.BusinessException;
import com.kevin.blog.common.result.Result;
import com.kevin.blog.common.result.ResultCode;
import com.kevin.blog.modules.article.dto.ArticleSaveDTO;
import com.kevin.blog.modules.article.entity.Article;
import com.kevin.blog.modules.article.entity.Tag;
import com.kevin.blog.modules.article.service.ArticleService;
import com.kevin.blog.modules.article.service.TagService;

import cn.hutool.core.bean.BeanUtil;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/articles")
public class AdminArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TagService tagService;

    @GetMapping
    public Result<List<Article>> list() {
        List<Article> articles = articleService.lambdaQuery()
                .orderByDesc(Article::getUpdateTime)
                .select(Article::getId, Article::getTitle, Article::getSlug,
                        Article::getStatus, Article::getIsTop, Article::getViewCount,
                        Article::getCategoryId, Article::getCreateTime, Article::getUpdateTime)
                .list();
        return Result.success(articles);
    }

    @GetMapping("/{id}")
    public Result<ArticleSaveDTO> getForEdit(@PathVariable Long id) {
        Article article = articleService.getById(id);
        if (article == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "文章不存在");
        }

        List<Tag> tags = tagService.getByArticleId(id);
        List<Long> tagIds = tags.stream().map(Tag::getId).toList();

        ArticleSaveDTO dto = new ArticleSaveDTO();
        BeanUtil.copyProperties(article, dto);
        dto.setTagIds(tagIds);

        return Result.success(dto);
    }

    @PostMapping
    public Result<Void> save(@Valid @RequestBody ArticleSaveDTO dto) {
        articleService.saveArticle(dto);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@Valid @RequestBody ArticleSaveDTO dto) {
        articleService.updateArticle(dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return Result.success();
    }
}
