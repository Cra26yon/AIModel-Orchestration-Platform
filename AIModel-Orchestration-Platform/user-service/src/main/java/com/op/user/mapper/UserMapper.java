package com.op.user.mapper;

import com.op.user.domain.po.User;
import com.op.user.domain.po.User_ApiKey;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from user where phone = #{phone}")
    User selectUserByPhone(String phone);

    @Select("select * from user_apikey where user_id = #{userId}")
    User_ApiKey selectApiKeyByUserId(Long userId);

    @Insert("insert into user(username,phone,created_at) values (#{username}, #{phone}, #{created_at})")
    void insertUser(User user);
}
