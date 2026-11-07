package com.qq.service;

import com.qq.pojo.PageBean;
import com.qq.pojo.dto.CommentDTO;
import com.qq.pojo.vo.CommentVO;

public interface CommentService {
    void addComment(CommentDTO commentDTO);

    PageBean<CommentVO> listComment(Integer pageNum, Integer pageSize, Integer size);

    void deleteComment(Integer userId);
}
