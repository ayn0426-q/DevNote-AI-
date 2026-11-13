package com.qq.controller;


import com.qq.pojo.KnowledgeDocument;
import com.qq.pojo.Result;
import com.qq.service.KnowledgeBaseService;
import com.qq.service.KnowledgeDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.ai.document.Document;
import java.util.List;

@RestController
@RequestMapping("/knowledge-base/{knowledgeBaseId}/documents")
public class KnowledgeDocumentController {
    @Autowired
    KnowledgeDocumentService knowledgeDocumentService;


    @PostMapping
    public Result addDocument(@PathVariable Integer knowledgeBaseId,
                              @RequestParam("file") MultipartFile file) {
        knowledgeDocumentService.addDocument(knowledgeBaseId, file);
        return Result.success();
    }


    @GetMapping("/{documentId}/content")
    public Result getDocumentContent(@PathVariable Integer knowledgeBaseId,
                                     @PathVariable Integer documentId) {
        String content=knowledgeDocumentService.getDocumentContent(knowledgeBaseId,documentId);
        return Result.success(content);
    }
    @GetMapping("/{documentId}/parse")
    public  Result getPdfContent(@PathVariable Integer knowledgeBaseId,
                                 @PathVariable Integer documentId){
        List<Document> getPdfcontent=knowledgeDocumentService.getPdfContent(knowledgeBaseId,documentId);
        return Result.success(getPdfcontent);
    }
}

