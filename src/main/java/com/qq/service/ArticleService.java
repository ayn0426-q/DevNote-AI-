package com.qq.service;

import com.qq.pojo.Article;
import com.qq.pojo.PageBean;
import com.qq.pojo.Tag;
import com.qq.pojo.dto.ArticleTagDTO;
import com.qq.pojo.vo.FavoriteVO;

import java.util.List;

public interface ArticleService {

    void add(Article article);

    PageBean list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    Article finByid(Integer id);

    void update(Article article);

    void delete(Integer id);

    List<Tag> findTags(Integer id);

    void fixTags(Integer id, ArticleTagDTO dto);

    void addFavorite(Integer id);

    void deleteFavorite(Integer articleId);

    PageBean<FavoriteVO> listFavorite(Integer pageNum, Integer pageSize);

}
