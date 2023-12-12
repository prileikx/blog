package cn.devsp.blog.dao;

import cn.devsp.blog.domain.Reply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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
public interface ReplyDao extends BaseMapper<Reply> {
    @Select("select reply.*,user.username,user.img from reply join user on user.uid = reply.uid where pid=#{pid} order by rid limit #{start},10")
    List<Reply> replyList(@Param("start")Integer start, @Param("pid")Integer pid);
}
