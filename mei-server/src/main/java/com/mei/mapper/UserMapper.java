package com.mei.mapper;

import com.mei.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM mei_take_out.user WHERE openid = #{openid} ;")
    User queryByOpenid(String openid);

    void insert(User user);

    @Select("SELECT * FROM mei_take_out.user WHERE id = #{id}")
    User queryById(Long id);

    List<User> queryListBetween(LocalDateTime begin, LocalDateTime end);

    Integer sumBefore(LocalDateTime of);

    Integer sumBetween(LocalDateTime begin, LocalDateTime end);
}
