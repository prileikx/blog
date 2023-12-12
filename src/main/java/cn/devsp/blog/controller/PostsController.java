package cn.devsp.blog.controller;


import cn.devsp.blog.common.R;
import cn.devsp.blog.domain.Posts;
import cn.devsp.blog.service.IPostclassService;
import cn.devsp.blog.service.IPostsService;
import cn.devsp.blog.service.impl.PostclassServiceImpl;
import cn.devsp.blog.service.impl.PostsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    private PostsServiceImpl postsService;
    @PostMapping("/postcount")
    public R postcount(@RequestParam String postclass,HttpServletRequest request){
        R r = postsService.postcount(postclass,request);
        return r;
    }
    @PostMapping("/getPostList/*")
    public R getPostList(HttpServletRequest request){
        return postsService.getPostList(request);
    }
    @PostMapping("/searchpost")
    public R searchpost(HttpServletRequest request){
        return postsService.searchpost(request);
    }
    @PostMapping("/searchpostcount")
    public R searchpostcount(@RequestParam String searchcontent,HttpServletRequest request){
        return postsService.searchpostcount(searchcontent,request);
    }
    @PostMapping("/postcontent/*")
    public R getPostContent(HttpServletRequest request, Integer page){
        return postsService.getPostContent(request,page);
    }
    @PostMapping("/sendpost")
    public R sendpost(HttpServletRequest request, String title, Integer pcid, String content){
        return postsService.sendpost(request,title,pcid,content);
    }
    @PostMapping("/deletepost")
    public R deletepost(HttpServletRequest request,Integer pid){
        return postsService.deletepost(request,pid);
    }
}

