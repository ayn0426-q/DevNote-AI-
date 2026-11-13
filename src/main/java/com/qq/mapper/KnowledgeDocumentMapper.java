package com.qq.mapper;

import com.qq.pojo.KnowledgeDocument;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface KnowledgeDocumentMapper {

    @Insert("INSERT INTO knowledge_document " +
            "(knowledge_base_id, original_name, stored_name, file_path, " +
            "file_type, file_size, parse_status, create_user) " +
            "VALUES " +
            "(#{knowledgeBaseId}, #{originalName}, #{storedName}, #{filePath}, " +
            "#{fileType}, #{fileSize}, #{parseStatus}, #{createUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(KnowledgeDocument document);


    @Select("select * from knowledge_document where id=#{documentId}")
    KnowledgeDocument findById(Integer documentId);
}