package com.qq.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qq.exception.BusinessException;
import com.qq.mapper.ArticleMapper;
import com.qq.mapper.CommentMapper;
import com.qq.pojo.Article;
import com.qq.pojo.Comment;
import com.qq.pojo.PageBean;
import com.qq.pojo.dto.CommentDTO;
import com.qq.pojo.vo.CommentVO;
import com.qq.service.ArticleService;
import com.qq.service.CommentService;
import com.qq.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
    public class CommentImpl implements CommentService {
        @Autowired
        private ArticleMapper articleMapper;
        @Autowired
        private CommentMapper commentMapper;
        @Override
        public void addComment(CommentDTO commentDTO) {
            if (commentDTO == null) {
                throw new BusinessException("请求参数不能为空");
            }
            if (commentDTO.getContent() == null ||
                    commentDTO.getContent().trim().isEmpty()) {
                throw new BusinessException("评论内容不能为空");
            }
            if (commentDTO.getArticleId() == null) {
                throw new BusinessException("文章为空无法评论");
            }
            Article article = articleMapper.finByid(commentDTO.getArticleId());
            if (article == null) {
                throw new BusinessException("文章不存在无法评论");
            }
            Map<String, Object> claims = ThreadLocalUtil.get();
            Integer userId = (Integer) claims.get("id");
            Comment comment = new Comment();
            comment.setArticleId(commentDTO.getArticleId());
            comment.setUserId(userId); comment.setContent(commentDTO.getContent());
            commentMapper.addComment(comment);
        }

    @Override
    public PageBean<CommentVO> listComment(Integer articleId,Integer pageNum, Integer pageSize) {
        Article article = articleMapper.finByid(articleId);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
            PageBean<CommentVO> pageBean=new PageBean<>();
            PageHelper.startPage(pageNum,pageSize);
            List<CommentVO> as =commentMapper.listComment(articleId);
            Page<CommentVO> c=(Page<CommentVO>) as;
            pageBean.setTotal(c.getTotal());
            pageBean.setItems(c.getResult());
            return pageBean;
    }

    @Override
    public void deleteComment(Integer commentId) {
       com.qq.pojo.Comment comment =commentMapper.findComment(commentId);
        if (comment==null){
            throw new BusinessException("该评论不存在无法查询");
        }
        Map<String,Object> claims=ThreadLocalUtil.get();
        Integer id=(Integer) claims.get("id");
        if (!comment.getUserId().equals(id)){
            throw new BusinessException("此评论不属于您，请勿操作");
        }
        commentMapper.deleteComment(commentId);
    }
}


