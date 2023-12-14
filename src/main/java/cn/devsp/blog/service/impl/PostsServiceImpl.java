package cn.devsp.blog.service.impl;

import cn.devsp.blog.common.R;
import cn.devsp.blog.dao.PostclassDao;
import cn.devsp.blog.dao.UserDao;
import cn.devsp.blog.dao.UsergroupDao;
import cn.devsp.blog.domain.Postclass;
import cn.devsp.blog.domain.Posts;
import cn.devsp.blog.dao.PostsDao;
import cn.devsp.blog.domain.User;
import cn.devsp.blog.service.IPostsService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Autowired
    private PostclassDao postclassDao;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UsergroupDao usergroupDao;
    @Autowired
    private PostsDao postsDao;
    @Override
    public R postcount(String postclass, HttpServletRequest request){
        LambdaQueryWrapper<Postclass> lqw = new LambdaQueryWrapper<Postclass>();
        lqw.eq(Postclass::getEnglishname,postclass);
        Postclass pc = postclassDao.selectOne(lqw);
        Integer pcid = pc.getPcid();
        Integer uid = userService.CheckloginStatus(request);
        Integer limits = 0;
        if (uid < 0) {
            limits = 50;
        } else {
            User user = userDao.selectById(uid);
            if (user != null) {
                limits = usergroupDao.selectById(user.getUgid()).getLimits();
            }
        }
        if (limits>=pc.getLimits()){
            if (pcid != null) {
                LambdaQueryWrapper<Posts> lqw2 = new LambdaQueryWrapper<Posts>();
                lqw2.eq(Posts::getPcid,pcid);
                Integer totalcount = postsDao.selectCount(lqw2);
                if (totalcount != null) {
                    if (totalcount==0){
                        totalcount=1;
                    }
                    return R.success(totalcount);
                } else {
                    return R.success(1);
                }
            } else {
                return R.success(1);
            }
        }else {
            return  R.success(1);
        }
    }
    @Override
    public R getPostList(HttpServletRequest request){
        String URI = request.getRequestURI();
        String pcenglishname = URI.substring(19);
        System.out.println("pcenglishname--------------->"+pcenglishname);
        LambdaQueryWrapper<Postclass> lqw = new LambdaQueryWrapper();
        lqw.eq(Postclass::getEnglishname,pcenglishname);
        Postclass pc = postclassDao.selectOne(lqw);
        Integer pcid = pc.getPcid();
        List<Posts> emptyPostList = new ArrayList<>();
        Posts emptyPost = new Posts();
        Integer uid = userService.CheckloginStatus(request);
        Integer limits = 0;
        R<List> r = new R<>();
        if (uid < 0) {
            limits = 50;
        } else {
            User user = userDao.selectById(uid);
            if (user != null) {
                limits = usergroupDao.selectById(user.getUgid()).getLimits();
            }
        }
        if (limits>=pc.getLimits()){
            if (pcid != null) {
                Integer page = Integer.valueOf(request.getParameter("page"));
                if (page==null){
                    page=1;
                }
                Integer start = (page-1)*10;
                List<Posts> posts = postsDao.selectpostList(pcid,start);

                if (posts != null && !posts.isEmpty()) {
                    r.setData(posts);
                    return r;
                } else {
                    emptyPost.setTitle("版区没有任何帖子");
                    emptyPost.setPcid(pcid);
                    emptyPostList.add(emptyPost);
                    r.setData(emptyPostList);
                    return r;
                }
            } else {
                emptyPost.setTitle("不存在的版区");
                emptyPost.setPcid(pcid);
                emptyPostList.add(emptyPost);
                r.setData(emptyPostList);
                return r;
            }
        }else {
            emptyPost.setTitle("访问权限不足");
            emptyPost.setPcid(pcid);
            emptyPostList.add(emptyPost);
            r.setData(emptyPostList);
            return r;
        }
    }
    @Override
    public R searchpost(HttpServletRequest request){
        List<Posts> emptyPostList = new ArrayList<>();
        Posts emptyPost = new Posts();
        Integer uid = userService.CheckloginStatus(request);
        Integer limits = 0;
        if (uid < 0) {
            limits = 40;
        } else {
            User user = userDao.selectById(uid);
            if (user != null) {
                limits = usergroupDao.selectById(user.getUgid()).getLimits();
            }
        }
        if (limits >= 50) {
            Integer page = Integer.valueOf(request.getParameter("page"));
            if (page == null) {
                page = 1;
            }
            Integer start = (page - 1) * 10;
            String searchcontent = request.getParameter("searchcontent");
            LambdaQueryWrapper<Posts> lqw2 = new LambdaQueryWrapper<Posts>();
            lqw2.like(Posts::getTitle,searchcontent).orderByDesc(Posts::getPid);
            IPage<Posts> page1 = new Page<Posts>(start,10);
            List<Posts> posts = postsDao.selectPage(page1,lqw2).getRecords();
            if (posts != null && !posts.isEmpty()) {
                String postList = JSON.toJSONString(posts);
                return R.success(postList);
            } else {
                emptyPost.setTitle("没有搜索到任何结果");
                emptyPost.setPcid(1);
                emptyPostList.add(emptyPost);
                String emptyPostListString = JSON.toJSONString(emptyPostList);
                return R.success(emptyPostListString);
            }
        } else {
            emptyPost.setTitle("权限不足,仅登录用户可使用搜索功能");
            emptyPost.setPcid(1);
            emptyPostList.add(emptyPost);
            String emptyPostListString = JSON.toJSONString(emptyPostList);
            return R.success(emptyPostListString);
        }
    }
    @Override
    public R searchpostcount(String searchcontent, HttpServletRequest request){
        List<Posts> emptyPostList = new ArrayList<>();
        Posts emptyPost = new Posts();
        Integer uid = userService.CheckloginStatus(request);
        Integer limits = 0;
        if (uid < 0) {
            limits = 40;
        } else {
            User user = userDao.selectById(uid);
            if (user != null) {
                limits = usergroupDao.selectById(user.getUgid()).getLimits();
            }
        }
        if (limits >= 50) {
            LambdaQueryWrapper<Posts> lqw = new LambdaQueryWrapper<>();
            lqw.like(Posts::getTitle,searchcontent);
            Integer totalcount = postsDao.selectCount(lqw);
            if (totalcount==0) {
                return R.success(1);
            } else {
                return R.success(totalcount);
            }
        } else {
            return R.success(1);
        }

    }
    @Override
    public R getPostContent(HttpServletRequest request, Integer page){
        if (page == null){
            page=1;
        }
        String URI = request.getRequestURI();
        String pid = URI.substring(19);
        R<Posts> r = new R<>();
        Posts emptyPost = new Posts();
        if (pid.equals("null")){
            emptyPost.setTitle("主题不存在");
            emptyPost.setContent("主题不存在");
            r.setData(emptyPost);
            return r;
        }
        Posts post = postsDao.selectPostContent(Integer.valueOf(pid));

        Integer uid = userService.CheckloginStatus(request);
        Integer limits = 0;
        if (uid < 0) {
            //默认不登录状态下权限为50
            limits = 50;
        } else {
            User user = userDao.selectById(uid);
            if (user != null) {
                limits = usergroupDao.selectById(user.getUgid()).getLimits();
            }
        }
        Integer pcid = post.getPcid();
        String poststring = JSON.toJSONString(post);
        if (limits >= postclassDao.selectById(pcid).getLimits()) {
            if (pcid != null) {
                Posts posts = new Posts();
                posts.setPid(Integer.valueOf(pid));
                posts.setViewcount(post.getViewcount()+1);
                postsDao.updateById(posts);
                r.setData(post);
                return r;
            } else {
                emptyPost.setTitle("主题不存在");
                emptyPost.setContent("主题不存在");
                r.setData(emptyPost);
                return r;
            }
        } else {
            emptyPost.setTitle("访问权限不足");
            emptyPost.setContent("访问权限不足");
            r.setData(emptyPost);
            return r;
        }
    }
    @Override
    public R sendpost(HttpServletRequest request, String title, Integer pcid, String content){
        Integer uid = userService.CheckloginStatus(request);
        if (uid < 0)
        {
            return R.success("登陆状态错误,请重新登录");
        }
        User user = userDao.selectById(uid);
        if (user == null){
            return R.success("登陆状态错误,请重新登录");
        }
        else{
            //检查用户权限是否大于等于版区权限
//            userGroupMapper.selectUserLimits(user.getUgid())>=postClassMapper.selectPostClassLimits(pcid)
            if (usergroupDao.selectById(user.getUgid()).getLimits()>=postclassDao.selectById(pcid).getLimits()){
                Posts post = new Posts();
                post.setUid(uid);
                post.setPcid(pcid);
                post.setContent(content);
                post.setTitle(title);
                post.setSendtime(LocalDateTime.now());
                postsDao.insert(post);
                user.setPostcount(user.getPostcount()+1);
                userDao.updateById(user);
                return R.success("发送成功");
            }
            else {
                return R.success("用户权限不足");
            }
        }
    }
    @Override
    public R deletepost(HttpServletRequest request, Integer pid){
        Integer uid = userService.CheckloginStatus(request);
        if(uid < 0)
        {
            return R.success("登录状态错误,请重新登录");
        }
        User user = userDao.selectById(uid);
        if (user == null){
            return R.success("登陆状态错误,请重新登录");
        }
        else{
            Posts posts = postsDao.selectById(pid);
            if (usergroupDao.selectById(user.getUgid()).getLimits()>=200 || user.getUid()==posts.getUid()){
                postsDao.deleteById(pid);
                return R.success("删除成功");
            }
            else {
                return R.success("用户权限不足");
            }
        }
    }
}
