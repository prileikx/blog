package cn.devsp.blog.service;

import cn.devsp.blog.common.R;
import cn.devsp.blog.domain.Posts;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author prileikx
 * @since 2023-12-03
 */
public interface IPostsService extends IService<Posts> {


    R postcount(String postclass, HttpServletRequest request);

    R getPostList(HttpServletRequest request);

    R searchpost(HttpServletRequest request);

    R searchpostcount(String searchcontent, HttpServletRequest request);

    R getPostContent(HttpServletRequest request, Integer page);

    R sendpost(HttpServletRequest request, String title, Integer pcid, String content);

    R deletepost(HttpServletRequest request, Integer pid);
}
