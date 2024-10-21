package com.aida.cat.mapper;

import com.aida.cat.model.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into user(login_id, login_pwd, nickname, tel) values(#{loginId}, #{loginPwd}, #{nickname}, #{tel})")
    int addUser(User user);

    @Delete("delete from user where login_id=#{loginId}")
    int delUser(String loginId);

    @Update("update user set login_pwd=#{loginPwd}, nickname=#{nickname}, tel=#{tel} where login_id=#{loginId}")
    int editUser(User user);

    @Select("select login_id loginId, login_pwd loginPwd, nickname, tel from user where login_id=#{loginId}")
    User findUserById(String loginId);

    @Select("select login_id loginId, login_pwd loginPwd, nickname, tel from user")
    List<User> findAllUser();

    @Select("select login_id loginId, nickname, tel from user where login_id=#{loginId} and login_pwd=#{loginPwd}")
    User findUserByIdAndPwd(User user);
}
