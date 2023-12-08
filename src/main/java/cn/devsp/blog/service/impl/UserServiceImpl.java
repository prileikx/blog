package cn.devsp.blog.service.impl;

import cn.devsp.blog.common.R;
import cn.devsp.blog.dao.UsergroupDao;
import cn.devsp.blog.domain.User;
import cn.devsp.blog.dao.UserDao;
import cn.devsp.blog.domain.Usergroup;
import cn.devsp.blog.service.IUserService;
import cn.devsp.blog.util.CheckCodeUtil;
import cn.devsp.blog.util.PasswordsaltUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

}
