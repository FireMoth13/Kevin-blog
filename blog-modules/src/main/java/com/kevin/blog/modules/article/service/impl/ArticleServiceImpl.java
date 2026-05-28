package com.kevin.blog.modules.article.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevin.blog.common.exception.BusinessException;
import com.kevin.blog.common.result.ResultCode;
import com.kevin.blog.modules.article.dto.ArticleQueryDTO;
import com.kevin.blog.modules.article.dto.ArticleSaveDTO;
import com.kevin.blog.modules.article.entity.Article;
import com.kevin.blog.modules.article.entity.ArticleTag;
import com.kevin.blog.modules.article.entity.Tag;
import com.kevin.blog.modules.article.mapper.ArticleMapper;
import com.kevin.blog.modules.article.mapper.ArticleTagMapper;
import com.kevin.blog.modules.article.service.ArticleService;
import com.kevin.blog.modules.article.service.TagService;
import com.kevin.blog.modules.article.vo.ArticleListVO;
import com.kevin.blog.modules.article.vo.ArticleVO;
import com.kevin.blog.modules.article.vo.TagBrief;



import cn.hutool.core.bean.BeanUtil;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;


    @Autowired
    private ArticleTagMapper articleTagMapper;

    //flexmark渲染器单例,把Markdown转成HTML的工具，Parser 解析 Markdown → 语法树，HtmlRenderer 语法树 → HTML
    private static final com.vladsch.flexmark.parser.Parser MD_PARSER=com.vladsch.flexmark.parser.Parser.builder().build();
    private static final com.vladsch.flexmark.html.HtmlRenderer MD_RENDERER=com.vladsch.flexmark.html.HtmlRenderer.builder().build();

    @Override
    @Transactional //事务，保证文章和标签关联的原子性
    public void saveArticle(ArticleSaveDTO dto){  //新增文章
        //1.生成slug
        String slug=generateSlug(dto.getTitle());
        
        //2.渲染Markdown成HTML
        String contentHtml=renderMarkdown(dto.getContentMd());

        //3.构建Article实体
        Article article=new Article();
        BeanUtil.copyProperties(dto, article);
        article.setSlug(slug);
        article.setContentHtml(contentHtml);
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        //4.保存文章
        save(article);

        //5.保存文章和标签的关联
        saveArticleTags(article.getId(), dto.getTagIds());
    }

    @Override
    @Transactional
    public void updateArticle(ArticleSaveDTO dto){  //更新文章
        //1.查原文章
        Article article=getById(dto.getId());
        if(article==null){
            throw new BusinessException(ResultCode.NOT_FOUND,"文章不存在");
        }

        //2.更新文章
        BeanUtil.copyProperties(dto, article);
        article.setContentHtml(renderMarkdown(dto.getContentMd()));
        article.setUpdateTime(LocalDateTime.now());
        updateById(article);

        //3.先删除原有标签关联，再保存新的标签关联
        deleteArticleTags(dto.getId());
        saveArticleTags(dto.getId(), dto.getTagIds());
    }

    @Override
    public IPage<ArticleListVO> pageArticles(ArticleQueryDTO dto) { //公开文章列表
        //1.分页查询文章
        Page<ArticleListVO> page=new Page<>(dto.getPageNum(),dto.getLimit());

        //2.如果有标签筛选，先查出符合标签条件的文章ID集合，拼进dto里的自定义的字段
        //没在ArticleQueryDTO里定义articleIds字段的话需要补一个
        if(dto.getTagId()!=null){
            List<Long> articleIds=articleMapper.selectArticleIdsByTagId(dto.getTagId());
            dto.setArticleIds(articleIds);
        }

        IPage<ArticleListVO> result=articleMapper.selectPageWithDetails(page, dto);

        //3.批量查标签：收集文章ID列表，一次性查出这些文章的标签，分配回每篇文章
        List<Long> articleIds=result.getRecords().stream()
                .map(ArticleListVO::getId).collect(Collectors.toList());
        if(!articleIds.isEmpty()){
            Map<Long,List<TagBrief>> tagMap=getTagMap(articleIds);
            result.getRecords().forEach(vo->vo.setTags(tagMap.getOrDefault(vo.getId(), List.of())));
        }
        return result;
    }

    @Override
    public ArticleVO getDetailBySlug(String slug){ //文章详情
        ArticleVO vo=articleMapper.selectDetailBySlug(slug);
        if(vo==null){
            throw new BusinessException(ResultCode.NOT_FOUND,"文章不存在");
        }

        //查标签
        List<Tag> tags=tagService.getByArticleId(vo.getId());
        vo.setTags(tags.stream()
                .map(t->new TagBrief(t.getId(),t.getName()))
                .collect(Collectors.toList()));

        //查上下篇
        vo.setPrev(articleMapper.selectPrev(vo.getCreateTime()));
        vo.setNext(articleMapper.selectNext(vo.getCreateTime()));
        return vo;
    }

    //私有辅助方法
    private String generateSlug(String title){
        //用Hutool拼音工具把标题转成拼音，作为slug
        String pinyin=cn.hutool.extra.pinyin.PinyinUtil.getPinyin(title,"");
        ////非字母数字替换成-，转小写
        pinyin=pinyin.replaceAll("[^a-zA-Z0-9]+","-").toLowerCase(); 
        return LocalDate.now().toString().replace("-","")+"-"+pinyin; //加个时间戳防止重复
        //结果示例：20260528-ni-hao-shi-jie
    }

    private String renderMarkdown(String md){
        return MD_RENDERER.render(MD_PARSER.parse(md));
    }

    private void saveArticleTags(Long articleId,List<Long> tagIds){
        if(tagIds==null || tagIds.isEmpty()){
            return;
        }
        List<ArticleTag> list=tagIds.stream()
                .map(tagId->{
                    ArticleTag at=new ArticleTag();
                    at.setArticleId(articleId);
                    at.setTagId(tagId);
                    return at;
                }).collect(Collectors.toList());
        articleTagMapper.insert(list);
    }

    private void deleteArticleTags(Long articleId){
        articleTagMapper.delete(
            new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleId)    
        );
    }

    private Map<Long,List<TagBrief>> getTagMap(List<Long> articleIds){
        // 批量查：IN 所有文章 ID，返回 Map<文章ID, 标签列表>
        //1.查中间表，拿到所有(articleId, tagId)对
        List<ArticleTag> articleTags=articleTagMapper.selectList(
            new LambdaQueryWrapper<ArticleTag>().in(ArticleTag::getArticleId, articleIds)
        );

        if(articleTags.isEmpty()){
            return Map.of();
        }

        //2.收集所有tag_id
        Set<Long> tagIds=articleTags.stream()
                .map(ArticleTag::getTagId)
                .collect(Collectors.toSet());
        
        //3.批量查标签, 拿tag详情
        List<Tag> tags=tagService.listByIds(tagIds);
        Map<Long,String> tagMap=tags.stream()
                .collect(Collectors.toMap(Tag::getId, Tag::getName));
        
        //4.组装成Map<文章ID, List<TagBrief>>
        return articleTags.stream()
                .collect(Collectors.groupingBy(
                    ArticleTag::getArticleId,
                    Collectors.mapping(
                        at->new TagBrief(at.getTagId(), tagMap.get(at.getTagId())),
                        Collectors.toList()
                    )
                ));

    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        deleteArticleTags(id); //先删除关联的标签
        removeById(id); //再删除文章
    }

    @Override
    public List<ArticleListVO> getArchives() {
        List<Article> articles=lambdaQuery().eq(Article::getStatus,1)
                .orderByDesc(Article::getCreateTime)
                .list();
        return articles.stream().map(a->{
            ArticleListVO vo=new ArticleListVO();
            vo.setId(a.getId());
            vo.setTitle(a.getTitle());
            vo.setSlug(a.getSlug());
            vo.setCreateTime(a.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public void incrViewCount(Long id) {
       lambdaUpdate().eq(Article::getId,id)
            .setSql("view_count = view_count + 1")
            .update();
    }   
}
