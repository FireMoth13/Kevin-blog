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

import com.kevin.blog.common.result.Result;
import com.kevin.blog.modules.article.entity.Category;
import com.kevin.blog.modules.article.service.CategoryService;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result<List<Category>> list() {
        return Result.success(categoryService.listAll());
    }

    @PostMapping
    public Result<Void> add(@RequestBody Category category) {
        categoryService.add(category);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Category category) {
        categoryService.update(category);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success();
    }
}