package cn.devsp.blog.service.impl;

import cn.devsp.blog.common.R;
import cn.devsp.blog.dao.CaptchaDao;
import cn.devsp.blog.dao.UsergroupDao;
import cn.devsp.blog.domain.Captcha;
import cn.devsp.blog.domain.User;
import cn.devsp.blog.dao.UserDao;
import cn.devsp.blog.service.IUserService;
import cn.devsp.blog.util.CheckCodeUtil;
import cn.devsp.blog.util.CheckEmailFormatUtil;
import cn.devsp.blog.util.IsLetterDigit;
import cn.devsp.blog.util.PasswordsaltUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author prileikx
 * @since 2023-12-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UsergroupDao usergroupDao;
    @Autowired
    private CaptchaDao captchaDao;
    @Override
    public R login(String username, String upassword, String ifsave, HttpServletResponse response) {
        User user = userDao.selectuserbyusername(username);
        if (user==null){
            user = userDao.selectuserbyemail(username);
        }
        if (user==null){
            return R.error("无此用户");
        }
        username = user.getUsername();
        upassword = PasswordsaltUtil.password(upassword,user);
        Integer uid = userDao.login(username,upassword);
        if (uid != null)
        {
            Cookie uidcookie = new Cookie("uid",Integer.toString(uid));
            String loginverifycode = CheckCodeUtil.generateVerifyCode(40);
            Cookie loginverifycodecookie = new Cookie("verifycode",loginverifycode);
            if (ifsave.equals("1"))
            {
                uidcookie.setMaxAge(60*60*24*30);
                loginverifycodecookie.setMaxAge(60*60*24*30);
            }
            uidcookie.setPath("/");
            loginverifycodecookie.setPath("/");
            response.addCookie(uidcookie);
            response.addCookie(loginverifycodecookie);
            userDao.addloginverify(uid,loginverifycode);
            R r = new R<>();
            r.add("uidcookie",uidcookie);
            r.add("loginverifycodecookie",loginverifycodecookie);
            return R.success("登录成功");
        }else {
            return R.error("用户名或密码错误");
        }


    }
    public Integer CheckloginStatus(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        Integer uid = null;
        String verifycode = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (name.equals("uid")) {
                    uid = Integer.valueOf(cookie.getValue());
                } else if (name.equals("verifycode")) {
                    verifycode = cookie.getValue();
                }
            }
        }else {
            //用户cookie为空(用户首次访问该网站)
            return -3;
        }
        if (uid==null || verifycode == null){
            //用户未登录,cookie中未保存登录信息
            return -1;
        }
        System.out.println(verifycode);
        User user = userDao.selectuserByidandverify(Integer.valueOf(uid), verifycode);
        if (user == null) {
            //无法查询到用户
            return -2;
        } else {
            //成功查询到用户,返回用户uid
            return uid;
        }
    }
    @Override
    public R getusername(HttpServletRequest request){
        R r=new R<User>();
//        CheckloginStatusUtil checkloginStatusUtil = new CheckloginStatusUtil();
//        Integer uid = checkloginStatusUtil.CheckloginStatus(request);
        Integer uid = CheckloginStatus(request);
        if(uid < 0){
            return R.error("登录信息过期");
//            writer.print("{\"userstatus\":\"400\"}");
        }else {
            User user = userDao.selectById(uid);
            String username = user.getUsername();
            r.add("username",username);
            r.add("uid",uid);
            r.setCode(1);
            return r;
//            writer.print("{\"userstatus\":200,\"username\":\""+username+"\",\"uid\":\""+uid+"\"}");
        }
    }
    @Override
    public R userinformation(String reuid, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        Integer uid = CheckloginStatus(request);
        R r = new R<User>();
        if (uid < 0) {
            User user = userDao.selectById(Integer.valueOf(reuid));
            r.setData(user);
        } else {
            User user = userDao.selectUserWithEmailByUid(Integer.valueOf(reuid));
//            String userjson = JSON.toJSONString(user);
            r.setData(user);
        }
        return r;
    }
    @Override
    public R changedescribe(HttpServletRequest request, String describe){
        Integer uid = CheckloginStatus(request);
        if (uid < 0) {
            return R.success("用户登录状态验证错误,请重新登录");
        } else {
            User user = userDao.selectById(uid);
            if (user != null) {
                user.setDescribes(describe);
                userDao.updateById(user);
                return R.success("修改成功");
            } else {
                return R.success("用户登录状态验证错误,请重新登录");
            }
        }
    }
    @Override
    public R changeemail(HttpServletRequest request, String password, String email){
        Integer uid = CheckloginStatus(request);
        if (uid < 0) {
            return R.success("用户登录状态验证错误,请重新登录");
        } else {
            User user = userDao.selectById(uid);
            if (user != null) {
                password = PasswordsaltUtil.password(password,user);
                if(userDao.getUpasswordByUidString(uid).equals(password)) {
                    if(CheckEmailFormatUtil.isEmail(email)){
                        user.setEmail(email);
                        userDao.updateById(user);
                        return R.success("修改成功");
                    }else {
                        return R.success("邮箱格式错误,请重新输入");
                    }
                }else {
                    return R.success("原密码输入错误,请重新输入");
                }
            }
            else {
                return R.success("原密码输入错误,请重新输入");
            }
        }
    }
    @Override
    public R changepassword(HttpServletRequest request){
        Integer uid = CheckloginStatus(request);
        if (uid < 0) {
            return R.success("用户登录状态验证错误,请重新登录");
        } else {
            User user = userDao.selectById(uid);
            if (user != null) {
                String password = request.getParameter("password");
                password = PasswordsaltUtil.password(password,user);
                if(userDao.getUpasswordByUidString(uid).equals(password)){
                    String afpassword = request.getParameter("afpassword");
                    afpassword = PasswordsaltUtil.password(afpassword,user);
                    user.setUpassword(afpassword);
                    userDao.updateById(user);
                    userDao.updateVerifySetNull(uid);
                    return R.success("修改成功,请重新登录");
                }else {
                    return R.success("原密码输入错误,请重新输入");
                }
            }
            else {
                return R.success("用户登录状态验证错误,请重新登录");
            }
        }
    }
    @Override
    public R changeusername(HttpServletRequest request){
        Integer uid = CheckloginStatus(request);
        if (uid < 0) {
            return R.success("用户登录状态验证错误,请重新登录");
        } else {
            User user = userDao.selectById(uid);
            if (user != null) {
                String username = request.getParameter("username");
                LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
                lqw.eq(User::getUsername,username);
                if(userDao.selectOne(lqw)==null){
                    user.setUsername(username);
                    userDao.updateById(user);
                    return R.success("修改成功");
                }else{
                    return R.success("该用户名已被其他人注册,请重新命名");
                }

            } else {
                return R.success("用户登录状态验证错误,请重新登录");
            }
        }
    }
    @Override
    public R register(HttpServletRequest request, HttpServletResponse response, String verify_code, String username) throws IOException {
        //获取验证码的值
        HttpSession session = request.getSession();
        String checkcode = (String)session.getAttribute("checkcodegen");
        if (checkcode==null){
            return R.success("我们无法比对此验证码,请点击'看不清?换一张'重新获取验证码输入");
        }
        //比对验证码
        if(!checkcode.equalsIgnoreCase(verify_code)){
            return R.success("验证码不正确");
        }
        if (username == null){
            return R.success("用户名不能为空");
        }
        String email = request.getParameter("email");
        if (email == null){
            return R.success("邮箱不能为空");
        }
        String email_verify = request.getParameter("email_verify");
        if (email_verify == null){
            return R.success("邮箱验证码不能为空");
        }
        System.out.println(5);
        String upassword = request.getParameter("upassword");
        if (upassword== null){
            return R.success("密码不能为空");
        }
        if (CheckEmailFormatUtil.isEmail(email)) {
//            userMapper.checkemailifwasregister(email)==0
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getEmail,email);
            if (userDao.selectOne(lqw)==null){
                LambdaQueryWrapper<Captcha> lqw1 = new LambdaQueryWrapper<>();
                lqw1.eq(Captcha::getEmail,email);
                if (!email_verify.equalsIgnoreCase(captchaDao.selectOne(lqw1).getCaptcha())) {
                    return R.success("邮箱验证码不正确");
                } else {
                    LambdaQueryWrapper<User> lqw2 = new LambdaQueryWrapper<>();
                    lqw2.eq(User::getUsername,username);
                    if (userDao.selectOne(lqw2)==null && username.length() > 2) {
                        User user = new User();
                        user.setUsername(username);
                        user.setUpassword(upassword);
                        user.setEmail(email);
                        user.setRegistertime(LocalDateTime.now());
                        userDao.insert(user);
                        LambdaQueryWrapper<User> lqw3 = new LambdaQueryWrapper<>();
                        lqw3.eq(User::getUsername,username);
                        User user1 = userDao.selectOne(lqw3);
                        Integer uid = user1.getUid();
                        Captcha captcha = new Captcha();
                        captcha.setUid(uid);
                        captcha.setEmail(email);
                        UpdateWrapper<Captcha> uw = new UpdateWrapper<>();
                        uw.eq("email",email);
                        uw.set("uid",uid);
                        captchaDao.update(null,uw);
                        upassword = PasswordsaltUtil.password(upassword,user1);
                        User user2 = new User();
                        user2.setUid(uid);
                        user2.setUpassword(upassword);
                        userDao.updateById(user2);
                        return R.success("注册成功");
                    } else {
                        return R.success("该用户名已被注册,请更换一个");
                    }
                }
            }else {
                return R.success("该邮箱已被注册,请更换一个,如忘记密码请使用找回密码功能");
            }
        }
        else {
            return R.success("邮箱格式不正确");
        }
    }
    @Override
    public R findpassword(HttpServletRequest request, String verify_code, String email, String email_verify, String upassword){
        //获取验证码的值
        HttpSession session = request.getSession();
        String checkcode = (String) session.getAttribute("checkcodegen");
        if (checkcode == null) {
            return R.success("我们无法比对此验证码,请点击'看不清?换一张'重新获取验证码输入");
        }
        //比对验证码
        if (!checkcode.equalsIgnoreCase(verify_code)) {
            return R.success("验证码不正确");
        }
        if (email == null) {
            return R.success("邮箱不能为空");
        }
        if (email_verify == null) {
            return R.success("邮箱验证码不能为空");
        }
        if (upassword == null) {
            return R.success("密码不能为空");
        }
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getEmail,email);
        User user = userDao.selectOne(lqw);
        if (user == null) {
            return R.success("该邮箱未曾注册过");
        }
        if (CheckEmailFormatUtil.isEmail(email)) {
            LambdaQueryWrapper<Captcha> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(Captcha::getEmail,email);
            if (!email_verify.equalsIgnoreCase(captchaDao.selectOne(lqw1).getCaptcha())) {
                return R.success("邮箱验证码不正确");
            } else {
                Integer uid = user.getUid();
                UpdateWrapper<Captcha> uw = new UpdateWrapper<>();
                uw.eq("email",email).set("uid",uid);
                captchaDao.update(null,uw);
                upassword = PasswordsaltUtil.password(upassword, user);
                user.setUpassword(upassword);
                userDao.updateById(user);
                return R.success("修改密码成功");

            }
        } else {
            return R.success("邮箱格式不正确");
        }
    }
    @Override
    public R logout(HttpServletRequest request){
        Integer uid = CheckloginStatus(request);
        if (uid < 0) {
            return R.success("已退出账号1");
        } else {
            User user = userDao.selectById(uid);
            if (user != null) {
                userDao.updateVerifySetNull(uid);
                return R.success("已退出账号2");
            }
            else {
                return R.success("已退出账号3");
            }
        }
    }
    @Override
    public R checkusernameifcanbeuse(String username){
        if (username == null||username.length()==0){
            return R.success("用户名不得为空");
        }
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUsername,username);
        User user = userDao.selectOne(lqw);
        if (user!=null){
            return R.success("用户名已被使用,请更换一个");
        } else if (username.length()<=2) {
            return R.success("用户名过短,请更换一个");
        }else if(username.length()>10){
            return R.success("用户名过长,请更换一个");
        }
        else if (!IsLetterDigit.stringchecknumal(username)) {
            return R.success("用户名只能由数字和字母或汉字构成");
        } else {
            return R.success("该用户名可以使用");
        }
    }
}
