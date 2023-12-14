package cn.devsp.blog.controller;


import cn.devsp.blog.common.R;
import cn.devsp.blog.service.impl.CollectServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/collect")
public class CollectController {
    @Autowired
    private CollectServiceImpl collectService;
    @PostMapping("/insertcollect")
    public R insertcollect(HttpServletRequest request,@RequestParam Integer pid){
        return collectService.insertcollect(request,pid);
    }
    @PostMapping("/deletecollect")
    public R deletecollection(HttpServletRequest request,@RequestParam Integer pid){
        return collectService.deletecollection(request,pid);
    }
    @PostMapping("/getusercollect")
    public R getusercollect(HttpServletRequest request, @RequestParam Integer page){
        return collectService.getusercollect(request,page);
    }
    @PostMapping("/getcollectcount")
    public R getcollectcount(HttpServletRequest request){
        return collectService.getcollectcount(request);
    }
}

