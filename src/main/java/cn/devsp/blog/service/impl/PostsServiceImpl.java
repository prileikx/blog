package cn.devsp.blog.service.impl;

import cn.devsp.blog.common.R;
import cn.devsp.blog.dao.PostclassDao;
import cn.devsp.blog.domain.Postclass;
import cn.devsp.blog.domain.Posts;
import cn.devsp.blog.dao.PostsDao;
import cn.devsp.blog.service.IPostsService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author prileikx
 * @since 2023-12-03
 */
@Service
public class PostsServiceImpl extends ServiceImpl<PostsDao, Posts> implements IPostsService {

}
