package cn.devsp.blog.controller;


import cn.devsp.blog.common.R;
import cn.devsp.blog.dao.UserDao;
import cn.devsp.blog.domain.User;
import cn.devsp.blog.service.IUserService;
import cn.devsp.blog.util.CheckloginStatusUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author prileikx
 * @since 2023-12-03
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private IUserService userService;

    @PostMapping("/loginServlet")
    public R login(@RequestParam String username, @RequestParam String upassword,@RequestParam String ifsave,HttpServletRequest request,HttpServletResponse response){
        R r = userService.login(username,upassword,ifsave,response);
        return r;
    }
    @PostMapping("/getusername")
    public R getusername(HttpServletRequest request){
        R r = userService.getusername(request);
        return r;
    }
    @PostMapping("/userinformation")
    public R userinformation(@RequestParam String reuid,HttpServletRequest request){
        R r = userService.userinformation(reuid,request);
        return r;
    }

}

