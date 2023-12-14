package cn.devsp.blog.service.impl;

import cn.devsp.blog.common.R;
import cn.devsp.blog.dao.UserDao;
import cn.devsp.blog.domain.Captcha;
import cn.devsp.blog.dao.CaptchaDao;
import cn.devsp.blog.domain.User;
import cn.devsp.blog.service.ICaptchaService;
import cn.devsp.blog.util.CheckCodeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author prileikx
 * @since 2023-12-03
 */
@Service
public class CaptchaServiceImpl extends ServiceImpl<CaptchaDao, Captcha> implements ICaptchaService {
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private UserDao userDao;
    @Override
    public R checkcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        ServletOutputStream servletOutputStream = response.getOutputStream();
        String checkCode = CheckCodeUtil.outputVerifyImage(100,50,servletOutputStream,4);
        session.setAttribute("checkcodegen",checkCode);
        response.getOutputStream().flush();
        response.getOutputStream().close();
        return null;
    }
    @Override
    public R sendfindpwdemail(String email){
        R r = emailService.sendSimpleMail(email, "论坛找回密码验证邮件", "您正在找回论坛密码,您的验证码为:");
        return r;
    }
    @Override
    public R sendregisteremail(String email){
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail,email);
        User user = userDao.selectOne(lqw);
        if(user == null){
            Integer sendmailbool = null;
            return emailService.sendSimpleMail(email,"论坛注册验证邮件","您正在注册论坛网站,您的验证码为:");
        } else {
            return R.success("邮箱已被注册");
        }
    }
}
