package org.interonet.mercury.domain.auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE username=#{username} AND password=#{password}")
    User selectByUsernamePassword(@Param("username") String username, @Param("password") String password);

    @Select("SELECT * FROM user WHERE username=#{username}")
    User selectByUsername(@Param("username") String username);
}
