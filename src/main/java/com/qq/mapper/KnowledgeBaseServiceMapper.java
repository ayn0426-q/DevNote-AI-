package com.qq.mapper;

import com.qq.pojo.KnowledgeBase;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface KnowledgeBaseServiceMapper {
    @Select("SELECT COUNT(*) FROM knowledge_base WHERE name = #{name} AND create_user = #{userId}")
    Integer testbase(@Param("name") String name,
                     @Param("userId") Integer userId);



    @Insert("INSERT INTO knowledge_base(name, description, create_user) " +
            "VALUES(#{name}, #{description}, #{createUser})")
    int addBase(KnowledgeBase knowledgeBase);

    @Select("SELECT * FROM knowledge_base " +
            "WHERE create_user = #{userId} " +
            "ORDER BY create_time DESC")
    List<KnowledgeBase> list(@Param("userId") Integer userId);



    @Update("UPDATE knowledge_base SET " +
            "name = #{name}, " +
            "description = #{description} " +
            "WHERE id = #{id} AND create_user = #{createUser}")
    int updatebase(KnowledgeBase knowledgeBase);



    @Select("SELECT * FROM knowledge_base " +
            "WHERE id = #{id}")
    KnowledgeBase findById(Integer id);

    @Select("SELECT COUNT(*) " +
            "FROM knowledge_base " +
            "WHERE name = #{name} " +
            "AND create_user = #{userId} " +
            "AND id != #{id}")
    Integer testUpdateName(@Param("name") String name,
                           @Param("userId") Integer userId,
                           @Param("id") Integer id);

    @Delete("delete from knowledge_base where id=#{id} and create_user=#{userId}")
    void deleteBase(Integer id, Integer userId);
}
