package com.qq.service.impl;

import com.qq.exception.BusinessException;
import com.qq.mapper.KnowledgeBaseServiceMapper;
import com.qq.mapper.KnowledgeDocumentMapper;
import com.qq.pojo.KnowledgeBase;
import com.qq.pojo.KnowledgeDocument;
import com.qq.service.KnowledgeDocumentService;
import com.qq.utils.ThreadLocalUtil;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ai.document.Document;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class KnowledgeDocumentImpl implements KnowledgeDocumentService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Mapper
    @Autowired
    KnowledgeDocumentMapper knowledgeDocumentMapper;
    @Autowired
    KnowledgeBaseServiceMapper knowledgeBaseServiceMapper;

    @Override
    public void addDocument(Integer id, MultipartFile file) {
        KnowledgeBase knowledgeBase = knowledgeBaseServiceMapper.findById(id);
        if (knowledgeBase == null) {
            throw new BusinessException("知识库不存在，请重新输入");
        }
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        if (!userId.equals(knowledgeBase.getCreateUser())) {
            throw new BusinessException("无权访问");
        }
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }
        if (file.getSize() > 10 * 1024 * 1024L) {
            throw new BusinessException("文件过大无法上传");
        }
        String originalName = file.getOriginalFilename();

        if (originalName == null || originalName.isBlank()
                || !originalName.contains(".")) {
            throw new BusinessException("文件名不合法");
        }

        int lastDotIndex = originalName.lastIndexOf(".");

        if (lastDotIndex == 0 || lastDotIndex == originalName.length() - 1) {
            throw new BusinessException("文件名不合法");
        }

        String suffix = originalName.substring(lastDotIndex + 1);
        if (!suffix.equalsIgnoreCase("pdf") && !suffix.equalsIgnoreCase("txt")) {
            throw new BusinessException("文件格式不符合要求，无法上传");
        }
        String storedName = UUID.randomUUID() + "." + suffix;
        Path targetPath;
        try {
            Path directory = Paths.get(uploadDir);
            Files.createDirectories(directory);
            targetPath = directory.resolve(storedName);
            file.transferTo(targetPath);
        } catch (IOException e) {
            throw new BusinessException("文件保存失败");
        }
        KnowledgeDocument document = new KnowledgeDocument();
        document.setKnowledgeBaseId(id);
        document.setOriginalName(originalName);
        document.setStoredName(storedName);
        document.setFilePath(targetPath.toString());
        document.setFileType(suffix.toUpperCase());
        document.setFileSize(file.getSize());
        document.setParseStatus("PENDING");
        document.setCreateUser(userId);
        try {
            knowledgeDocumentMapper.insert(document);
        } catch (RuntimeException e) {

            try {
                Files.deleteIfExists(targetPath);
            } catch (IOException ex) {

            }

            throw new BusinessException("文档信息保存失败");
        }
    }

    @Override
    public String getDocumentContent(Integer knowledgeBaseId, Integer documentId) {
        KnowledgeDocument knowledgeDocument =
                checkDocumentAccess(knowledgeBaseId, documentId);
        if (!"txt".equalsIgnoreCase(knowledgeDocument.getFileType())) {
            throw new BusinessException("文件类型不符合，无法读取");
        }
        Path path = checkFilePath(knowledgeDocument.getFilePath());
        String content = null;
        try {
            content = Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new BusinessException("文件读取失败");
        }
        if (content.isBlank()) {
            throw new BusinessException("文件内容为空");
        }
        return content;
    }

    @Override
    public List<Document> getPdfContent(Integer knowledgeBaseId, Integer documentId) {
        KnowledgeDocument knowledgeDocument =
                checkDocumentAccess(knowledgeBaseId, documentId);
        if (!"pdf".equalsIgnoreCase(knowledgeDocument.getFileType())) {
            throw new BusinessException("文件类型不符合，无法读取");
        }
        Path path = checkFilePath(knowledgeDocument.getFilePath());
        FileSystemResource resource = new FileSystemResource(path);
        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                .withPageTopMargin(0)
                .withPageBottomMargin(0)
                .withPageExtractedTextFormatter(
                        ExtractedTextFormatter.builder()
                                .withNumberOfTopTextLinesToDelete(0)
                                .build()
                )
                .withPagesPerDocument(1)
                .build();
        PagePdfDocumentReader reader =
                new PagePdfDocumentReader(resource, config);
        List<org.springframework.ai.document.Document> documents = reader.read();
        if (documents.isEmpty()) {
            throw new BusinessException("文件内容为空");
        }
        boolean allBlank = documents.stream()
                .allMatch(doc -> doc.getText() == null || doc.getText().isBlank());
        if (allBlank) {
            throw new BusinessException("文本内容为空");
        }
        return documents;
    }

    @Override
    public List<Document> getDocumentAsDocuments(
            Integer knowledgeBaseId,
            Integer documentId) {

        // 1. 检查文档权限
        KnowledgeDocument document =
                checkDocumentAccess(knowledgeBaseId, documentId);

        // 2. 获取文件类型
        String fileType = document.getFileType();

        // 3. 根据文件类型解析
        if ("txt".equalsIgnoreCase(fileType)) {

            String content = getDocumentContent(
                    knowledgeBaseId, documentId);

            Document aiDocument = new Document(content);

            return List.of(aiDocument);

        } else if ("pdf".equalsIgnoreCase(fileType)) {

            return getPdfContent(knowledgeBaseId, documentId);

        } else {
            throw new BusinessException("不支持的文件类型");
        }
    }

    private Path checkFilePath(String filePath) {

        if (filePath == null || filePath.isBlank()) {
            throw new BusinessException("文件路径不存在");
        }

        Path uploadRoot = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();

        Path path = Paths.get(filePath)
                .toAbsolutePath()
                .normalize();

        if (!path.startsWith(uploadRoot)) {
            throw new BusinessException("文件路径不合法");
        }

        if (!Files.exists(path)) {
            throw new BusinessException("文件不存在，无法读取");
        }

        if (!Files.isRegularFile(path)) {
            throw new BusinessException("文件路径有误");
        }

        return path;
    }

    private KnowledgeDocument checkDocumentAccess(
            Integer knowledgeBaseId,
            Integer documentId) {

        KnowledgeBase knowledgeBase =
                knowledgeBaseServiceMapper.findById(knowledgeBaseId);

        if (knowledgeBase == null) {
            throw new BusinessException("知识库不存在，无法查看");
        }

        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");

        if (!knowledgeBase.getCreateUser().equals(userId)) {
            throw new BusinessException("无权查看该知识库");
        }

        KnowledgeDocument document =
                knowledgeDocumentMapper.findById(documentId);

        if (document == null) {
            throw new BusinessException("文档不存在，无法读取");
        }

        if (!document.getKnowledgeBaseId().equals(knowledgeBaseId)) {
            throw new BusinessException("文档不属于该知识库，无法读取");
        }

        return document;
    }
}

