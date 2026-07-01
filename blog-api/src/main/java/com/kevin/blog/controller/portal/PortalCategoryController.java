package com.kevin.blog.controller.portal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.blog.common.result.Result;
import com.kevin.blog.modules.article.entity.Category;
import com.kevin.blog.modules.article.service.CategoryService;

@RestController
@RequestMapping("/api/public")
public class PortalCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public Result<List<Category>> list() {
        return Result.success(categoryService.listAll());
    }
}
