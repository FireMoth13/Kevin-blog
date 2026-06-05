package com.kevin.blog.controller.portal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kevin.blog.common.result.Result;
import com.kevin.blog.modules.article.dto.ArticleQueryDTO;
import com.kevin.blog.modules.article.service.ArticleService;
import com.kevin.blog.modules.article.vo.ArticleListVO;
import com.kevin.blog.modules.article.vo.ArticleVO;

@RestController
@RequestMapping("/api/public")
public class PortalArticleController {
    @Autowired
    private ArticleService articleService;

    //文章列表
    @GetMapping("/articles")
    public Result<IPage<ArticleListVO>> listArticles(ArticleQueryDTO dto){  
        return Result.success(articleService.pageArticles(dto));
    }

    @GetMapping("/articles/{slug}")
    public Result<ArticleVO> detail(@PathVariable String slug){
        return Result.success(articleService.getDetailBySlug(slug));
    }

    //归档
    @GetMapping("/archives")
    public Result<List<ArticleListVO>> archives(){
        return Result.success(articleService.getArchives());
    }

    //浏览量+1
    @PutMapping("/articles/{id}/view")
    public Result<Void> incrView(@PathVariable Long id){
        articleService.incrViewCount(id);
        return Result.success();
    }
}
