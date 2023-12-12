package cn.devsp.blog.controller;


import cn.devsp.blog.common.R;
import cn.devsp.blog.service.impl.ReplyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
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
@RequestMapping("/reply")
public class ReplyController {
    @Autowired
    private ReplyServiceImpl replyService;
    @RequestMapping("/replycount")
    public R replycount(@RequestParam String pid, HttpServletRequest request){
        return replyService.replycount(pid,request);
    }
    @GetMapping("/getReplyList")
    public R getReply(@RequestParam Integer page, HttpServletRequest request, @RequestParam Integer pid){
        System.out.println("pid---------------------------------->"+pid);
        return replyService.getReply(page,request,pid);
    }
    @RequestMapping("/sendreply")
    public R sendreply(HttpServletRequest request,@RequestParam Integer pid,@RequestParam String content,@RequestParam Integer replyuid){
        return replyService.sendreply(request,pid,content,replyuid);
    }
    @RequestMapping("/deletereply")
    public R deletereply(HttpServletRequest request,@RequestParam Integer rid){
        return replyService.deletereply(request,rid);
    }
}

