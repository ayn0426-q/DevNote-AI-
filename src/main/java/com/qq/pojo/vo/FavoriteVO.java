package com.qq.pojo.vo;


import lombok.Data;

import java.time.LocalDateTime;
@Data
public class FavoriteVO {
    private  Integer id;
    private String title;
    private String content;
    private String coverImg;
    private  LocalDateTime createTime;
    private LocalDateTime favoriteTime;
}
