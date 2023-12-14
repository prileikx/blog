package cn.devsp.blog.service.impl;

import cn.devsp.blog.common.R;
import cn.devsp.blog.dao.*;
import cn.devsp.blog.domain.Collect;
import cn.devsp.blog.domain.Posts;
import cn.devsp.blog.domain.User;
import cn.devsp.blog.service.ICollectService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
public class CollectServiceImpl extends ServiceImpl<CollectDao, Collect> implements ICollectService {
    @Autowired
    private PostsDao postsDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UsergroupDao usergroupDao;
    @Autowired
    private PostclassDao postclassDao;
    @Autowired
    private CollectDao collectDao;
    @Override
    public R insertcollect(HttpServletRequest request, Integer pid){
        Cookie[] cookies = request.getCookies();
        String uid = null;
        String verifycode = null;
        if(cookies!=null){
            for (Cookie cookie:cookies){
                String name = cookie.getName();
                if (name.equals("uid")){
                    uid = cookie.getValue();
                } else if (name.equals("verifycode")) {
                    verifycode = cookie.getValue();
                }
            }
        }else
        {
            return R.success("登录状态错误,请重新登录");
        }
        Posts posts = postsDao.selectById(pid);
        Integer pcid = posts.getPcid();
        User user = userDao.selectById(Integer.valueOf(uid));
        String userverify = userDao.selectVerifyByUid(Integer.valueOf(uid));
        if (user == null||userverify==null||!userverify.equals(verifycode)){
            return R.success("登录状态错误,请重新登录");
        }
        else{
            //检查用户权限是否大于等于版区权限
//            userGroupMapper.selectUserLimits(user.getUgid())>=postClassMapper.selectPostClassLimits(pcid)
            if (usergroupDao.selectById(user.getUgid()).getLimits()>=postclassDao.selectById(pcid).getLimits()){
//                collectMapper.selectifcollect(Integer.valueOf(uid),pid)==null
                LambdaQueryWrapper<Collect> lqw1 = new LambdaQueryWrapper<>();
                lqw1.eq(Collect::getUid,uid).eq(Collect::getPid,pid);
                if(collectDao.selectOne(lqw1)==null){
                    Collect collect = new Collect();
                    collect.setPid(pid);
                    collect.setUid(Integer.valueOf(uid));
                    collectDao.insert(collect);
                    return R.success("收藏成功");
                }else {
                    return R.success("已经收藏过了,是否取消收藏?");
                }
            }
            else {
                return R.success("用户权限不足");
            }
        }
    }
    @Override
    public R deletecollection(HttpServletRequest request, Integer pid){
        Cookie[] cookies = request.getCookies();
        String uid = null;
        String verifycode = null;
        if(cookies!=null){
            for (Cookie cookie:cookies){
                String name = cookie.getName();
                if (name.equals("uid")){
                    uid = cookie.getValue();
                } else if (name.equals("verifycode")) {
                    verifycode = cookie.getValue();
                }
            }
        }else
        {
            return R.success("登录状态错误,请重新登录");
        }
        Integer pcid = postsDao.selectById(pid).getPcid();
        User user = userDao.selectById(Integer.valueOf(uid));
        String userverify = userDao.selectVerifyByUid(Integer.valueOf(uid));
        if (user == null||userverify==null||!userverify.equals(verifycode)){
            return R.success("登录状态错误,请重新登录");
        }
        else{
            //检查用户权限是否大于等于版区权限
            if (usergroupDao.selectById(user.getUgid()).getLimits()>=postclassDao.selectById(pcid).getLimits()){
                LambdaQueryWrapper<Collect> lqw1 = new LambdaQueryWrapper<>();
                lqw1.eq(Collect::getUid,Integer.valueOf(uid)).eq(Collect::getPid,pid);
                Collect collect = collectDao.selectOne(lqw1);
                if(collect!=null) {
                    collectDao.deleteById(collect);
                    return R.success("取消收藏成功");
                }
                else {
                    return R.success("该帖子未被用户收藏");
                }
            }
            else {
                return R.success("用户权限不足");
            }
        }
    }
    @Override
    public R getusercollect(HttpServletRequest request, Integer page){
        Cookie[] cookies = request.getCookies();
        String uid = null;
        String verifycode = null;
        List<Collect> emptycollectlist = new ArrayList<>();
        Collect emptycollect = new Collect();
        String emtpycollectjson = "";
        R<List> r = new R<>();
        if(cookies!=null){
            for (Cookie cookie:cookies){
                String name = cookie.getName();
                if (name.equals("uid")){
                    uid = cookie.getValue();
                } else if (name.equals("verifycode")) {
                    verifycode = cookie.getValue();
                }
            }
        }else
        {
            emptycollect.setTitle("登陆状态错误,请重新登录");
            emptycollectlist.add(emptycollect);
            r.setData(emptycollectlist);
            return r;
        }
        User user = userDao.selectById(Integer.valueOf(uid));
        String userverify = userDao.selectVerifyByUid(Integer.valueOf(uid));
        if (user == null||userverify==null||!userverify.equals(verifycode)){
            emptycollect.setTitle("登陆状态错误,请重新登录");
            emptycollectlist.add(emptycollect);
            r.setData(emptycollectlist);
            return r;
        }
        else{
            if (page == null){
                page=1;
            }
            List<Collect> collects = collectDao.selectrusercollect(Integer.valueOf(uid),(page-1)*10);
            if(collects==null||collects.isEmpty()){
                emptycollect.setTitle("您未收藏任何帖子哦~");
                emptycollectlist.add(emptycollect);
                r.setData(emptycollectlist);
                return r;
            }else {
                r.setData(collects);
                return r;
            }
        }
    }
    @Override
    public R getcollectcount(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String uid = null;
        String verifycode = null;
        if(cookies!=null){
            for (Cookie cookie:cookies){
                String name = cookie.getName();
                if (name.equals("uid")){
                    uid = cookie.getValue();
                } else if (name.equals("verifycode")) {
                    verifycode = cookie.getValue();
                }
            }
        }else
        {
            return R.success(1);
        }
        User user = userDao.selectById(Integer.valueOf(uid));
        String userverify = userDao.selectVerifyByUid(Integer.valueOf(uid));
        if (user == null||userverify==null||!userverify.equals(verifycode)){
            return R.success(1);
        }
        else{
            LambdaQueryWrapper<Collect> lqw1 = new LambdaQueryWrapper<>();
            lqw1.eq(Collect::getUid,uid);
            Integer totalcount = collectDao.selectCount(lqw1);
            return R.success(totalcount);
        }
    }
}
