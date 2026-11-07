package com.qq.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qq.exception.BusinessException;
import com.qq.mapper.ArticleFavoriteMapper;
import com.qq.mapper.ArticleMapper;
import com.qq.pojo.*;
import com.qq.pojo.dto.ArticleTagDTO;
import com.qq.pojo.vo.FavoriteVO;
import com.qq.service.ArticleService;
import com.qq.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ArticleImpl implements ArticleService {
@Autowired
private ArticleMapper articleMapper;
@Autowired
private ArticleFavoriteMapper articleFavoriteMapper;
    @Override
    public void add(Article article) {
        if (article.getVisibility() == null) {
            article.setVisibility("private");
        }

        if (!article.getVisibility().equals("private")
                && !article.getVisibility().equals("public")) {
            throw new BusinessException("文章可见范围不合法");
        }

        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
         Map<String,Object> map=ThreadLocalUtil.get();
        Integer userId=(Integer) map.get("id");
        article.setCreateUser(userId);
        articleMapper.add(article);
    }

    @Override
    public PageBean list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        PageBean<Article> pageBean=new PageBean<>();
        PageHelper.startPage(pageNum,pageSize);
        Map<String,Object> map=ThreadLocalUtil.get();
        Integer userId=(Integer) map.get("id");
        List<Article> as= articleMapper.list(userId,categoryId,state);
        Page<Article> p=(Page<Article> )as;
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());
        return pageBean;
    }

    @Override
    public Article finByid(Integer id) {
        Article article=articleMapper.finByid(id);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        Map<String,Object> claims=ThreadLocalUtil.get();
        Integer userId=(Integer) claims.get("id");

        if (!article.getCreateUser().equals(userId)){
           if (!"public".equals(article.getVisibility())
            || !"已发布".equals(article.getState())){
            throw   new BusinessException("无权限查看该文章详情");
            }
        }

        return article;
    }

    @Override
    public void update(Article article) {
        Map<String,Object> claims=ThreadLocalUtil.get();
        Integer userId=(Integer) claims.get("id");
        Integer count=articleMapper.findTagfrom(article.getId(),userId);
        if (count==0){
            throw new BusinessException("该文章不属于该用户无法查询");
        }
        if (article.getVisibility() != null
                && !article.getVisibility().equals("private")
                && !article.getVisibility().equals("public")) {
            throw new BusinessException("文章可见范围不合法");
        }
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article);

    }

    @Override
    public void delete(Integer id) {
        articleMapper.delete(id);

    }

    @Override
    public List<Tag> findTags(Integer id) {

        Map<String,Object> claims=ThreadLocalUtil.get();
        Integer userId=(Integer) claims.get("id");
        Integer count=articleMapper.findTagfrom(id,userId);
        if (count==0){
            throw new BusinessException("该文章不属于该用户无法查询");
        }
        List<Tag> tg=articleMapper.findTag(id);
        return tg;
    }

    @Override
    @Transactional
    public void fixTags(Integer id, ArticleTagDTO dto) {
        Map<String,Object> claims=ThreadLocalUtil.get();
        Integer userId=(Integer) claims.get("id");
        Integer count=articleMapper.findTagfrom(id,userId);
        if (dto==null){
            throw  new BusinessException("不能返回空标签");
        }
        if (dto.getTagIds()==null){
            throw  new BusinessException("标签集合不能为空");
        }
        if (count==0){
            throw new BusinessException("该文章不属于该用户无法进行修改");
        }
        if (!dto.getTagIds().isEmpty()) {
            Integer counts = articleMapper.findTagsSave(dto.getTagIds());

            if (counts != dto.getTagIds().size()) {
                throw new BusinessException("存在不存在的标签");
            }
        }
        articleMapper.deleteTags(id);

        if (!dto.getTagIds().isEmpty()) {
            articleMapper.updateTags(dto.getTagIds(), id);
        }

    }

    @Override

    public void addFavorite(Integer id) {
        Article article = articleMapper.finByid(id);
        Map<String,Object> claims=ThreadLocalUtil.get();
        Integer userid=(Integer) claims.get("id");
        if (article==null){
            throw new BusinessException("文章不存在无法添加收藏");
        }
        if (article.getCreateUser().equals(userid)==false && !"public".equals(article.getVisibility()) ){
            throw  new BusinessException("当前用户无权限访问文章");
        }
        Integer count= articleFavoriteMapper.testFavorite(id,userid);
        if (count>0){
            throw  new BusinessException("该文章已经收藏过了无法再次添加");
        }
        articleFavoriteMapper.addFavorite(id,userid);
    }

    @Override
    public void deleteFavorite(Integer articleId) {
        Map<String,Object> claims=ThreadLocalUtil.get();
        Integer userid=(Integer) claims.get("id");
        Integer count=articleFavoriteMapper.deleteFavorite(userid,articleId);
        if (count==0){
            throw  new BusinessException("该文章尚未收藏无法删除收藏");
        }
    }

    @Override
    public PageBean<FavoriteVO> listFavorite(Integer pageNum, Integer pageSize) {
        PageBean<FavoriteVO> pageBean=new PageBean<>();
        PageHelper.startPage(pageNum,pageSize);
        Map<String,Object> map=ThreadLocalUtil.get();
        Integer userId=(Integer) map.get("id");
        List<FavoriteVO> as= articleFavoriteMapper.list(userId);
        Page<FavoriteVO> p=(Page<FavoriteVO> )as;
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());
        return pageBean;
    }


}
