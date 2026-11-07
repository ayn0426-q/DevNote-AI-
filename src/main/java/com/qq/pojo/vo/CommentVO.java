package com.qq.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CommentVO {
    private Integer id;
    private Integer articleId;
    private Integer userId;
    private  String username;
    private  String content;
    private LocalDateTime createTime;
}
