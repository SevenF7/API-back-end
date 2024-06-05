package apiwork.mapper;

import apiwork.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {
//
//    //根据用户名查询用户
//    @Select("select * from user where username=#{username}")
//    User findByUserName(String username);
//
//    //添加
//    @Insert("insert into  user(username,nickname,email,password,create_time)"+//,
//    " values(#{username},#{nickname},#{email},#{password},now())")//,
//    void add(String username,String nickname,String email, String password);
//
//    @Update("update  user set nickname=#{nickname},email=#{email}where id=#{id}")
//    void update(User user);
//
//    @Update("update user set user_pic=#{avatarUrl} where id=#{id}")
//    void updateAvatar(Integer id, String avatarUrl);
//
//    @Update("update user set password=#{newPwd} where id=#{id}")
//    void updatePwd(Integer id, String newPwd);
}
