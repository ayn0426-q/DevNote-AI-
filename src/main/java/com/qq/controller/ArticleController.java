package com.qq.controller;

import com.qq.pojo.Article;
import com.qq.pojo.PageBean;
import com.qq.pojo.Result;
import com.qq.pojo.Tag;
import com.qq.pojo.dto.ArticleTagDTO;
import com.qq.pojo.vo.FavoriteVO;
import com.qq.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Result add(@RequestBody @Validated Article article) {
        articleService.add(article);
        return Result.success();
    }

    @GetMapping
    public Result<PageBean<Article>> list(
            Integer pageNum, Integer pageSize, @RequestParam(required = false) Integer categoryId, @RequestParam(required = false) String state
    ) {
        PageBean pb = articleService.list(pageNum, pageSize, categoryId, state);
        return Result.success(pb);

    }


    @GetMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Article article = articleService.finByid(id);
        return Result.success(article);
    }

    @PutMapping
    public Result update(@RequestBody @Validated Article article) {
        articleService.update(article);
        return Result.success(article);
    }


    @DeleteMapping
    public Result delete(@RequestParam Integer id) {
        articleService.delete(id);
        return Result.success();
    }

    @GetMapping("/{id}/tags")
    public Result findTags(@PathVariable Integer id) {
        List<Tag> tg = articleService.findTags(id);
        return Result.success(tg);
    }

    @PutMapping("/{id}/tags")
    public Result fixTags(@PathVariable Integer id, @RequestBody ArticleTagDTO dto) {
        articleService.fixTags(id, dto);
        return Result.success();
    }

    @PostMapping("/{id}/favorite")
    public Result addFavorite(@PathVariable Integer id) {
        articleService.addFavorite(id);
        return Result.success("已成功添加收藏");
    }

    @DeleteMapping("/favorite/{articleId}")
    public Result deleteFavorite(@PathVariable Integer articleId) {
        articleService.deleteFavorite(articleId);
        return Result.success("已成功取消收藏");
    }

    @GetMapping("/favorite")
    public Result<PageBean<FavoriteVO>> listFavorite(
            Integer pageNum, Integer pageSize){
     {
            PageBean<FavoriteVO> pb = articleService.listFavorite(pageNum, pageSize);
            return Result.success(pb);
        }
    }

}

