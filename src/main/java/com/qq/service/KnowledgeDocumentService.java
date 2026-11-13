package com.qq.service;

import org.springframework.web.multipart.MultipartFile;

import org.springframework.ai.document.Document;
import java.util.List;

public interface KnowledgeDocumentService {
    void addDocument(Integer id, MultipartFile file);

    String getDocumentContent(Integer knowledgeBaseId, Integer documentId);

    List<Document> getPdfContent(Integer knowledgeBaseId, Integer documentId);

    List<Document> getDocumentAsDocuments(
            Integer knowledgeBaseId,
            Integer documentId
    );
}



