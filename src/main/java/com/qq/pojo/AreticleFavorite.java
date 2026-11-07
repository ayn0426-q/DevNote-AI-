package com.qq.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AreticleFavorite {
    private Long id;

    private Long articleId;

    private Long userId;

    private LocalDateTime createTime;
}
