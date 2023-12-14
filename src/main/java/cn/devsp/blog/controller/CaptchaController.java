package cn.devsp.blog.controller;


import cn.devsp.blog.common.R;
import cn.devsp.blog.service.impl.CaptchaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/captcha")
public class CaptchaController {
    @Autowired
    private CaptchaServiceImpl captchaService;
    @RequestMapping("/checkcode")
    public R checkcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return captchaService.checkcode(request,response);
    }
    @RequestMapping("/sendfindpwdemail")
    public R sendfindpwdemail(@RequestParam String email){
        return captchaService.sendfindpwdemail(email);
    }
    @RequestMapping("/sendregisteremail")
    public R sendregisteremail(@RequestParam String email){
        return captchaService.sendregisteremail(email);
    }
}

