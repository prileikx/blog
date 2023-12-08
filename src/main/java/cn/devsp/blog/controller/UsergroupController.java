package cn.devsp.blog.controller;


import cn.devsp.blog.common.R;
import cn.devsp.blog.dao.UserDao;
import cn.devsp.blog.dao.UsergroupDao;
import cn.devsp.blog.service.impl.UsergroupServiceImpl;
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
@RequestMapping("/usergroup")
public class UsergroupController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UsergroupDao usergroupDao;
    @Autowired
    private UsergroupServiceImpl usergroupService;
    @PostMapping("/usergroup")
    public R usergroup(@RequestParam String reuid){
        R r = usergroupService.usergroup(reuid);
        return r;
    }
}

