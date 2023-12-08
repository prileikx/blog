package cn.devsp.blog.dao;

import cn.devsp.blog.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author prileikx
 * @since 2023-12-03
 */
@Repository
public interface UserDao extends BaseMapper<User> {
    @Select("select * from user where uid = #{uid} and verify = #{verify}")
    User selectuserByidandverify(@Param("uid")Integer uid,@Param("verify")String verify);
    @Select("select * from user where email=#{email}")
    User selectuserbyemail(String email);
    @Select("select * from user where username=#{username}")
    User selectuserbyusername(@Param("username")String username);
    @Select("select uid from user where username = #{username} and upassword = #{upassword}")
    Integer login(@Param("username")String username,@Param("upassword")String upassword);
    @Update("update user set verify = #{verify} where uid=#{uid};")
    void addloginverify(@Param("uid")Integer uid,@Param("verify")String verify);
    @Select("select uid,username,describes,ugid,email,registertime,ifconfirmemail,replycount,postcount,img from user where uid = #{uid}")
    User selectUserWithEmailByUid(@Param("uid")Integer uid);



}
