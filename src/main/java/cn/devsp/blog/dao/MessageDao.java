package cn.devsp.blog.dao;

import cn.devsp.blog.domain.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author prileikx
 * @since 2023-12-03
 */
@Repository
public interface MessageDao extends BaseMapper<Message> {
    @Select("select message.*,posts.title,user.username from message,user,posts where user.uid=message.whoreplyuid and posts.pid=message.pid and message.uid=#{uid} or user.uid=message.whoreplyuid and posts.pid=message.pid and message.uid=10 order by mid desc limit #{start},10")
    List<Message> getMessageList(@Param("uid")Integer uid, @Param("start")Integer start);
    @Select("insert into message (msg, uid, sendtime,pid,whoreplyuid) values (#{msg},10,now(),1,10)")
    Integer insertpublicmsg(@Param("msg")String msg);
}
