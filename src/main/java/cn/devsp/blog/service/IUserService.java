package cn.devsp.blog.service;

import cn.devsp.blog.common.R;
import cn.devsp.blog.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    R changedescribe(HttpServletRequest request, String describe);
    R changeemail(HttpServletRequest request, String password, String email);
    R changepassword(HttpServletRequest request);
    R changeusername(HttpServletRequest request);
    R register(HttpServletRequest request, HttpServletResponse response, String verify_code, String username) throws IOException;
    R findpassword(HttpServletRequest request, String verify_code, String email, String email_verify, String upassword);
    R logout(HttpServletRequest request);

    R checkusernameifcanbeuse(String username);
}
