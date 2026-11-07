package com.qq.mapper;

import com.qq.pojo.Result;
import com.qq.pojo.Tag;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TagMapper {
    @Insert("insert into tag (tag_name,create_user,create_time,update_time) values(#{tagName},#{createUser},now(),now())")
    void add(Tag tag);


    @Select("select  count(*) from tag where tag_name=#{tagName} and create_user=#{createUser}")
    Integer testRepeat(@Param("tagName") String tagName, @Param("createUser") Integer createUser);

    @Select("select * from tag where create_user=#{createUser}")
    List<Tag> findTag(Integer userId);

    @Select("SELECT COUNT(*) FROM tag WHERE id = #{id} AND create_user = #{userId}")
    Integer testid(@Param("id") Integer id,
                   @Param("userId") Integer userId);


    @Update("UPDATE tag SET tag_name = #{tagName} WHERE id = #{id} AND create_user = #{userId}")
    void fixTag(Integer id, String tagName, Integer userId);



    @Delete("DELETE FROM article_tag WHERE tag_id = #{id}")
    void deleteabout(Integer id);
    @Delete("DELETE FROM tag WHERE id = #{id} AND create_user = #{userId}")
    void deleteTag(Integer id, Integer userId);
}



