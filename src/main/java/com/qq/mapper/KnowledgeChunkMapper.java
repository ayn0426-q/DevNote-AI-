package com.qq.mapper;

import com.qq.pojo.KnowledgeChunk;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface KnowledgeChunkMapper {

    @Delete("DELETE FROM knowledge_chunk WHERE document_id = #{documentId}")
    void deleteByDocumentId(Integer documentId);

    @Insert("<script>" +
            "INSERT INTO knowledge_chunk " +
            "(document_id, knowledge_base_id, chunk_index, content, metadata) " +
            "VALUES " +
            "<foreach collection='chunks' item='chunk' separator=','>" +
            "(#{chunk.documentId}, " +
            "#{chunk.knowledgeBaseId}, " +
            "#{chunk.chunkIndex}, " +
            "#{chunk.content}, " +
            "#{chunk.metadata})" +
            "</foreach>" +
            "</script>")
    int insertBatch(@Param("chunks") List<KnowledgeChunk> chunks);

    @Select("SELECT COUNT(*) FROM knowledge_chunk WHERE document_id = #{documentId}")
    int countByDocumentId(Integer documentId);
}
