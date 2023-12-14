package cn.devsp.blog.dao;

import cn.devsp.blog.domain.Captcha;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author prileikx
 * @since 2023-12-03
 */
@Repository
public interface CaptchaDao extends BaseMapper<Captcha> {
    @Select("select TIMESTAMPDIFF(SECOND,(select sendtime from captcha where email=#{email}),now());")
    Integer checksendtime(@Param("email")String email);
}
