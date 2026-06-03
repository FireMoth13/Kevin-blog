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
import com.kevin.blog.modules.article.entity.Tag;
import com.kevin.blog.modules.article.service.TagService;

@RestController
@RequestMapping("/api/admin/tags")
public class TagController {
    @Autowired
    private TagService tagService;
    
    @GetMapping
    public Result<List<Tag>> list(){
        return Result.success(tagService.listAll());
    }

    @PostMapping
    public Result<Void> add(@RequestBody Tag tag){
        tagService.add(tag);
        return Result.success();
    }
    
    @PutMapping
    public Result<Void> update(@RequestBody Tag tag){
        tagService.update(tag);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id){
        tagService.delete(id);
        return Result.success();
    }
}
