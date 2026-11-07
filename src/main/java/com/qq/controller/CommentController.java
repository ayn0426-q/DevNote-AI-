package com.qq.controller;

import com.qq.pojo.Comment;
import com.qq.pojo.PageBean;
import com.qq.pojo.Result;
import com.qq.pojo.dto.CommentDTO;
import com.qq.pojo.vo.CommentVO;
import com.qq.pojo.vo.FavoriteVO;
import com.qq.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    public CommentService commentService;


    @PostMapping()
    public Result addComment(@RequestBody CommentDTO commentDTO){
        commentService.addComment(commentDTO);
        return Result.success("添加成功");
    }

    @GetMapping("/article/{articleId}")
    public Result<PageBean<CommentVO>> listcomment(@PathVariable Integer articleId,
                                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                                   @RequestParam(defaultValue = "10") Integer pageSize){
        PageBean<CommentVO> pb = commentService.listComment(articleId,pageNum, pageSize);
        return Result.success(pb);
    }
    @DeleteMapping("/{commentId}")
    public Result deleteComment(@PathVariable Integer commentId){
        commentService.deleteComment(commentId);
        return Result.success("删除成功");
    }
}
