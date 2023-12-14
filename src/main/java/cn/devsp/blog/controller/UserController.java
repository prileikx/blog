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
import java.io.IOException;

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
        return userService.login(username,upassword,ifsave,response);
    }
    @PostMapping("/getusername")
    public R getusername(HttpServletRequest request){
        return userService.getusername(request);
    }
    @PostMapping("/userinformation")
    public R userinformation(@RequestParam String reuid,HttpServletRequest request){
        return userService.userinformation(reuid,request);
    }
    @PostMapping("/changedescribe")
    public R changedescribe(HttpServletRequest request,@RequestParam String describe){
        return userService.changedescribe(request,describe);
    }
    @PostMapping("/changeemail")
    public R changeemail(HttpServletRequest request,@RequestParam String password,@RequestParam String email){
        return userService.changeemail(request,password,email);
    }
    @PostMapping("/changepassword")
    public R changepassword(HttpServletRequest request){
        return userService.changepassword(request);
    }
    @PostMapping("/changeusername")
    public R changeusername(HttpServletRequest request){
        return userService.changeusername(request);
    }
    @PostMapping("/register")
    public R register(HttpServletRequest request, HttpServletResponse response,@RequestParam String verify_code,@RequestParam String username) throws IOException {
        return userService.register(request,response,verify_code,username);
    }
    @PostMapping("/findpassword")
    public R findpassword(HttpServletRequest request,@RequestParam String verify_code,@RequestParam String email,@RequestParam String email_verify,@RequestParam String upassword){
        return userService.findpassword(request,verify_code,email,email_verify,upassword);
    }
    @RequestMapping("/logout")
    public R logout(HttpServletRequest request){
        return userService.logout(request);
    }
    @RequestMapping("/checkusernameifcanbeuse")
    public R checkusernameifcanbeuse(@RequestParam String username){
        return userService.checkusernameifcanbeuse(username);
    }


}

