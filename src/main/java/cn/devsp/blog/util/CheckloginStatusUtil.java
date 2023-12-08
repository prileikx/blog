package cn.devsp.blog.util;


import cn.devsp.blog.dao.UserDao;
import cn.devsp.blog.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CheckloginStatusUtil {
    @Autowired
    private UserDao userDao;
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
}
