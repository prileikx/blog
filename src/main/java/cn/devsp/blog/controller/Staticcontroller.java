package cn.devsp.blog.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOError;
import java.io.IOException;

@RestController
public class Staticcontroller {
    /**
     * 将根路径重定向到index.html
     * @param resp
     * @return
     * @throws IOException
     */
    @RequestMapping("/")
    public void index(HttpServletResponse resp) throws IOException{
        //重定向,会改变网址
        resp.sendRedirect("/index.html");
    }
    @RequestMapping("/user/msg/{uid}")
    public void usermsg(HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException {
        //转发,不会改变网址
        request.getRequestDispatcher("/user/msg.html").forward(request,response);
    }
}
