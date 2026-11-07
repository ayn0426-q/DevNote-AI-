package com.qq.pojo;

import java.time.LocalDateTime;
import  lombok.Data;


@Data
public class Tag {
    private Integer id;
    private  String tagName;
    private  Integer createUser;
    private LocalDateTime createTime;
    private  LocalDateTime updateTime;

}
