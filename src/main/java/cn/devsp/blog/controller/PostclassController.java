package cn.devsp.blog.controller;


import cn.devsp.blog.common.R;
import cn.devsp.blog.service.IPostclassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author prileikx
 * @since 2023-12-03
 */
@RestController
@RequestMapping("/postclass")
public class PostclassController {
    @Autowired
    private IPostclassService postclassService;
    @PostMapping("/selectPostpcnameServlet")
    public R selectPostpcnameServlet(){
        R r = postclassService.selectpostname();
        return r;
    }
}

