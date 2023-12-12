package cn.devsp.blog.service.impl;

import cn.devsp.blog.common.R;
import cn.devsp.blog.dao.*;
import cn.devsp.blog.domain.Message;
import cn.devsp.blog.domain.Posts;
import cn.devsp.blog.domain.Reply;
import cn.devsp.blog.domain.User;
import cn.devsp.blog.service.IReplyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
public class ReplyServiceImpl extends ServiceImpl<ReplyDao, Reply> implements IReplyService {
    @Autowired
    private PostsDao postsDao;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UsergroupDao usergroupDao;
    @Autowired
    private PostclassDao postclassDao;
    @Autowired
    private ReplyDao replyDao;

    @Autowired
    private UserServiceImpl userService;
    @Override
    public R replycount(String pid, HttpServletRequest request){
        Integer pcid = postsDao.selectById(pid).getPcid();
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
        if (limits>=postclassDao.selectById(pcid).getLimits()){
            if (pcid != null) {
                LambdaQueryWrapper<Reply> lqw = new LambdaQueryWrapper<>();
                lqw.eq(Reply::getPid,pid);
                Integer totalcount = replyDao.selectCount(lqw);
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
            return R.success(1);
        }
    }
    @Override
    public R getReply(Integer page, HttpServletRequest request, Integer pid){
        if (page==null){
            page=1;
        }
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
        Posts post=postsDao.selectById(pid);
        Integer pcid = post.getPcid();
        Reply emptyReply = new Reply();
        List<Reply> emptyReplyList = new ArrayList<>();
        R<List> r = new R<List>();
        if (limits >= postclassDao.selectById(pcid).getLimits()) {
            if (pcid != null) {
                Integer start = (page-1)*10;
                List<Reply> replyList = replyDao.replyList(start,pid);
                r.setData(replyList);
                return r;
            } else {
                emptyReply.setContent("主题不存在");
                emptyReplyList.add(emptyReply);
                r.setData(emptyReplyList);
                return r;
            }
        } else {
            emptyReply.setContent("访问权限不足");
            emptyReplyList.add(emptyReply);
            r.setData(emptyReplyList);
            return r;
        }
    }
    @Override
    public R sendreply(HttpServletRequest request, Integer pid, String content, Integer replyuid){
        Integer uid = userService.CheckloginStatus(request);
        if(uid < 0)
        {
            return R.success("登录状态错误,请重新登录");
        }
        if (request.getParameter("pid").equals("undefined")){
            return R.success("回复的帖子不存在");
        }
        Integer pcid = postsDao.selectById(pid).getPcid();
        User user = userDao.selectById(uid);
        if (user == null){
            return R.success("登陆状态错误,请重新登录");
        }
        else{
            //检查用户权限是否大于等于版区权限
            if (usergroupDao.selectById(user.getUgid()).getLimits()>=postclassDao.selectById(pcid).getLimits()){
                if (replyuid!=0){
                    Message message = new Message();
                    message.setMsg("在以下帖子中回复了您:");
                    message.setPid(pid);
                    message.setWhoreplyuid(replyuid);
                    message.setUid(uid);
                    message.setSendtime(LocalDateTime.now());
                    messageDao.insert(message);
                }
                Reply reply = new Reply();
                reply.setUid(uid);
                reply.setPid(pid);
                reply.setContent(content);
                reply.setSendtime(LocalDateTime.now());
                replyDao.insert(reply);
                user.setReplycount(user.getReplycount()+1);
                userDao.updateById(user);
                Posts posts = postsDao.selectById(pid);
                posts.setReplycount(posts.getReplycount()+1);
                postsDao.updateById(posts);
                return R.success("发送成功");
            }
            else {
                return R.success("用户权限不足");
            }
        }
    }
    @Override
    public R deletereply(HttpServletRequest request, Integer rid){
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
            Reply reply = replyDao.selectById(rid);
//            userGroupMapper.selectUserLimits(user.getUgid())>=200 || user.getUid()==reply.getUid()
            if (usergroupDao.selectById(user.getUgid()).getLimits()>=200 || user.getUid()==reply.getUid()){
                replyDao.deleteById(rid);
                return R.success("删除成功");
            }
            else {
                return R.success("用户权限不足");
            }
        }
    }
}
