package com.qq.pojo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class KnowledgeChunk {
    private Integer id;
    private Integer documentId;
    private Integer knowledgeBaseId;
    private Integer chunkIndex;
    private String content;
    private String metadata;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
