package com.qq.mapper;

import com.qq.pojo.Comment;
import com.qq.pojo.vo.CommentVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("INSERT INTO comment(article_id, user_id, content) " +
            "VALUES(#{articleId}, #{userId}, #{content})")
    void addComment(Comment comment);

    @Select("SELECT c.id, c.article_id, c.user_id, " +
            "u.username, c.content, c.create_time " +
            "FROM `comment` c " +
            "JOIN `user` u ON c.user_id = u.id " +
            "WHERE c.article_id = #{articleId} " +
            "ORDER BY c.create_time DESC")
    List<CommentVO> listComment(Integer articleId);


    @Select("SELECT id, article_id, user_id, content, create_time, update_time " +
            "FROM `comment` " +
            "WHERE id = #{commentId}")
    Comment findComment(@Param("commentId") Integer commentId);

    @Delete("DELETE FROM `comment` WHERE id = #{commentId}")
    void deleteComment(@Param("commentId") Integer commentId);
}
