package cn.devsp.blog.service;

import cn.devsp.blog.common.R;
import cn.devsp.blog.domain.Captcha;
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
public interface ICaptchaService extends IService<Captcha> {

    R checkcode(HttpServletRequest request, HttpServletResponse response) throws IOException;

    R sendfindpwdemail(String email);

    R sendregisteremail(String email);
}
