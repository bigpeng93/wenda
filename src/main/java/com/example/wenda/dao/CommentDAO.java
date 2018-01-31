package com.example.wenda.dao;


import com.example.wenda.model.Comment;
import com.example.wenda.model.LoginTicket;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where entity_id=#{entityId} and " +
            " entity_Type = #{entityType} order by created_date desc "})
    List<Comment> selectCommentByEntity(@Param("entityId") int userId,
                                         @Param("entityType") int offset);

    @Select({"select count(id) from ",TABLE_NAME," where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("id") int id,@Param("commentCount") int commentCount);

    @Update({"update comment set status = #{status} where id=#{id}"})
    int updateStatus(@Param("id") int id,@Param("status") int status);

}
