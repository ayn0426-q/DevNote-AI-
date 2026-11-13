package com.qq.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qq.exception.BusinessException;
import com.qq.mapper.KnowledgeBaseServiceMapper;
import com.qq.mapper.KnowledgeChunkMapper;
import com.qq.mapper.KnowledgeDocumentMapper;
import com.qq.pojo.KnowledgeBase;
import com.qq.pojo.KnowledgeChunk;
import com.qq.pojo.KnowledgeDocument;
import com.qq.pojo.vo.KnowledgeChunkVO;
import com.qq.service.KnowledgeChunkService;
import com.qq.service.KnowledgeDocumentService;
import com.qq.utils.ThreadLocalUtil;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.transaction.annotation.Transactional;
@Service
public class KnowledgeChunkServiceImpl implements KnowledgeChunkService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    KnowledgeChunkMapper knowledgeChunkMapper;
    @Autowired
    KnowledgeDocumentMapper knowledgeDocumentMapper;
    @Autowired
    KnowledgeBaseServiceMapper knowledgeBaseServiceMapper;
    @Autowired
    private KnowledgeDocumentService knowledgeDocumentService;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor =Exception.class)
    public KnowledgeChunkVO documentChunk(Integer documentId) {
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        KnowledgeDocument knowledgeDocument = knowledgeDocumentMapper.findById(documentId);

        if (knowledgeDocument == null) {
            throw new BusinessException("文档不存在无法提取");
        }

        if (knowledgeDocument.getKnowledgeBaseId() == null) {
            throw new BusinessException("文档关联知识库不存在，无法提取");
        }
        KnowledgeBase knowledgeBase = knowledgeBaseServiceMapper.findById(knowledgeDocument.getKnowledgeBaseId());
        if (knowledgeBase == null) {
            throw new BusinessException("知识库不存在");
        }
        if (!knowledgeBase.getCreateUser().equals(userId)) {
            throw new BusinessException("无权访问该知识库");
        }
        List<Document> documents = knowledgeDocumentService.getDocumentAsDocuments(
                knowledgeDocument.getKnowledgeBaseId(),
                documentId
        );
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> chunks = splitter.split(documents);
        if (chunks == null || chunks.isEmpty()) {
            throw new BusinessException("文档解析失败");
        }
        List<KnowledgeChunk> knowledgeChunks = new ArrayList<>();
        for (int i = 0; i < chunks.size(); i++) {
            Document chunk = chunks.get(i);

            if (chunk.getText() == null || chunk.getText().isBlank()) {
                throw new BusinessException("分块内容不能为空");
            }

            KnowledgeChunk knowledgeChunk = new KnowledgeChunk();
            knowledgeChunk.setDocumentId(documentId);
            knowledgeChunk.setKnowledgeBaseId(knowledgeBase.getId());
            knowledgeChunk.setChunkIndex(i);
            knowledgeChunk.setContent(chunk.getText());

            try {
                String metadata = objectMapper.writeValueAsString(chunk.getMetadata());
                knowledgeChunk.setMetadata(metadata);
            } catch (JsonProcessingException e) {
                throw new BusinessException("Metadata转换失败");
            }

            knowledgeChunks.add(knowledgeChunk);
        }
        knowledgeChunkMapper.deleteByDocumentId(documentId);

        knowledgeChunkMapper.insertBatch(knowledgeChunks);

        Integer chunkCount = knowledgeChunkMapper.countByDocumentId(documentId);

        KnowledgeChunkVO vo = new KnowledgeChunkVO();
        vo.setDocumentId(documentId);
        vo.setChunkCount(chunkCount);

        return vo;
    }
}
