package com.qq.pojo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class KnowledgeBase {
    private Integer id;

    private String name;

    private String description;

    private Integer createUser;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
