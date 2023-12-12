package cn.devsp.blog.dao;

import cn.devsp.blog.domain.Posts;
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
public interface PostsDao extends BaseMapper<Posts> {
    @Select("select * from posts where pcid=#{pcid} order by pid desc limit #{start},10")
    List<Posts> selectpostList(@Param("pcid")Integer pcid, @Param("start")Integer start);
}
