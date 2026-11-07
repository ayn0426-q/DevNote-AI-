package com.qq.mapper;


import com.qq.pojo.vo.FavoriteVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleFavoriteMapper {


    @Select("select count(*) from favorite where article_id=#{id} and user_id=#{userId}")
    Integer testFavorite(@Param("id") Integer id, @Param("userId") Integer userId);


    @Insert("insert into favorite(article_id,user_id,create_time) values(#{articleId},#{userId},now())")
     void addFavorite(@Param("articleId") Integer articleId, @Param("userId") Integer userId);


    @Delete("delete  from favorite where user_id=#{userId} and article_id=#{articleId}")
    Integer deleteFavorite(@Param("userId") Integer userId, @Param("articleId") Integer articleId);


    @Select("SELECT\n" +
            "    a.id,\n" +
            "    a.title,\n" +
            "    a.content,\n" +
            "    a.cover_img,\n" +
            "    a.create_time,\n" +
            "    f.create_time AS favorite_time\n" +
            "FROM favorite f\n" +
            "JOIN article a ON f.article_id = a.id\n" +
            "WHERE f.user_id = #{userId}\n" +
            "ORDER BY f.create_time DESC")
    List<FavoriteVO> list(Integer userId);
}
