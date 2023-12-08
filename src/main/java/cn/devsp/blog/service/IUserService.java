package cn.devsp.blog.service;

import cn.devsp.blog.common.R;
import cn.devsp.blog.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author prileikx
 * @since 2023-12-03
 */
public interface IUserService extends IService<User> {
    R login(String username, String upassword, String ifsave, HttpServletResponse response);
    R getusername(HttpServletRequest request);
    R userinformation(String reuid, HttpServletRequest request);

}
