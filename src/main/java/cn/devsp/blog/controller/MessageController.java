package cn.devsp.blog.controller;


import cn.devsp.blog.common.R;
import cn.devsp.blog.dao.MessageDao;
import cn.devsp.blog.service.impl.MessageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author prileikx
 * @since 2023-12-03
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageServiceImpl messageService;
    @RequestMapping("/getMessageCount")
    public R getMessageCount(HttpServletRequest request){
        return messageService.getMessageCount(request);
    }
    @RequestMapping("/getMessageList")
    public R getMessageList(HttpServletRequest request,@RequestParam Integer page){
        return messageService.getMessageList(request,page);
    }
    @RequestMapping("/sendpublicmsg")
    public R sendpublicmsg(HttpServletRequest request,@RequestParam String publicmsg_content){
        return messageService.sendpublicmsg(request,publicmsg_content);
    }
}

