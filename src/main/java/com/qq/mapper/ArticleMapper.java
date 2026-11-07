package com.qq.mapper;

import com.qq.pojo.Article;
import com.qq.pojo.Tag;
import com.qq.pojo.dto.ArticleTagDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Insert("INSERT INTO article\n" +
            "(title, content, cover_img, state, category_id,\n" +
            " create_user, create_time, update_time, visibility)\n" +
            "VALUES\n" +
            "(#{title}, #{content}, #{coverImg}, #{state}, #{categoryId},\n" +
            " #{createUser}, #{createTime}, #{updateTime}, #{visibility})")
    void add(Article article);


    @Select("<script>" +
            "SELECT * FROM article " +
            "WHERE (create_user = #{userId} " +
            "OR (visibility = 'public' AND state = '已发布')) " +
            "<if test='categoryId != null'> " +
            "AND category_id = #{categoryId} " +
            "</if> " +
            "<if test='state != null and state != \"\"'> " +
            "AND state = #{state} " +
            "</if> " +
            "ORDER BY update_time DESC " +
            "</script>")
    List<Article> list(
            @Param("userId") Integer userId,
            @Param("categoryId") Integer categoryId,
            @Param("state") String state
    );


    @Select("select * from article where id=#{id}")
    Article finByid(Integer id);



    @Update("<script>" +
            "UPDATE article " +
            "SET title=#{title}, " +
            "content=#{content}, " +
            "cover_img=#{coverImg}, " +
            "state=#{state}, " +
            "category_id=#{categoryId}, " +
            "update_time=#{updateTime} " +
            "<if test='visibility != null'>, visibility=#{visibility}</if> " +
            "WHERE id=#{id}" +
            "</script>")
    void update(Article article);


    @Delete("delete  from article where id=#{id}")
    void delete(Integer id);


    @Select("SELECT COUNT(*) FROM article WHERE id = #{articleId} AND create_user = #{userId}")
    Integer findTagfrom(@Param("articleId") Integer articleId,
                        @Param("userId") Integer userId);


    @Select("SELECT t.id, t.tag_name " +
            "FROM article_tag at " +
            "JOIN tag t ON at.tag_id = t.id " +
            "WHERE at.article_id = #{id}")
    List<Tag> findTag(Integer id);



    @Select({
            "<script>",
            "SELECT COUNT(*)",
            "FROM tag",
            "WHERE id IN",
            "<foreach collection='tagIds' item='tagId' open='(' separator=',' close=')'>",
            "#{tagId}",
            "</foreach>",
            "</script>"
    })
    Integer findTagsSave(@Param("tagIds") List<Integer> tagIds);

    @Delete("DELETE FROM article_tag WHERE article_id = #{id}")
    void deleteTags(Integer id);


    @Insert("<script>" +
            "INSERT INTO article_tag (article_id, tag_id, create_time) VALUES " +
            "<foreach collection='tagIds' item='tagId' separator=','>" +
            "(#{articleId}, #{tagId}, NOW())" +
            "</foreach>" +
            "</script>")
    void updateTags(@Param("tagIds") List<Integer> tagIds,
                    @Param("articleId") Integer articleId);
}
