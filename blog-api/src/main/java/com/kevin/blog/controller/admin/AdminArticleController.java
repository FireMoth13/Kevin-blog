package com.kevin.blog.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.blog.common.result.Result;
import com.kevin.blog.modules.article.dto.ArticleSaveDTO;
import com.kevin.blog.modules.article.service.ArticleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/articles")
public class AdminArticleController {

    @Autowired
    private ArticleService articleService;

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