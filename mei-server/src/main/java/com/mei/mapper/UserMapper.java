package com.mei.mapper;

import com.mei.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM mei_take_out.user WHERE openid = #{openid} ;")
    User queryByOpenid(String openid);

    void insert(User user);
}
