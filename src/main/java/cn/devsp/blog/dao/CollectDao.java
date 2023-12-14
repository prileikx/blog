package cn.devsp.blog.dao;

import cn.devsp.blog.domain.Collect;
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
public interface CollectDao extends BaseMapper<Collect> {
    @Select("select posts.title,collect.* from collect join posts on posts.pid=collect.pid where collect.uid=#{uid} order by cid desc LIMIT #{start},10")
    List<Collect> selectrusercollect(@Param("uid")Integer uid, @Param("start")Integer start);
}
