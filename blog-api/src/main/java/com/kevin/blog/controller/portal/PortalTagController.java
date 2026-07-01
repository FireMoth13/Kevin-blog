package com.kevin.blog.controller.portal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.blog.common.result.Result;
import com.kevin.blog.modules.article.entity.Tag;
import com.kevin.blog.modules.article.service.TagService;

@RestController
@RequestMapping("/api/public")
public class PortalTagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public Result<List<Tag>> list() {
        return Result.success(tagService.listAll());
    }
}
