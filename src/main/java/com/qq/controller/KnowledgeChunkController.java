package com.qq.controller;

import com.qq.pojo.Result;
import com.qq.pojo.vo.KnowledgeChunkVO;
import com.qq.service.KnowledgeChunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/knowledge/document")
public class KnowledgeChunkController {
    @Autowired
    KnowledgeChunkService knowledgeChunkService;

    @PostMapping("/{documentId}/chunks")
    public Result documentChunk(@PathVariable Integer documentId){
       KnowledgeChunkVO knowledgeChunkVO= knowledgeChunkService.documentChunk(documentId);
        return Result.success(knowledgeChunkVO);
    }
}
