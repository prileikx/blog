package cn.devsp.blog.service.impl;

import cn.devsp.blog.common.R;
import cn.devsp.blog.dao.UserDao;
import cn.devsp.blog.dao.UsergroupDao;
import cn.devsp.blog.domain.Message;
import cn.devsp.blog.dao.MessageDao;
import cn.devsp.blog.domain.User;
import cn.devsp.blog.service.IMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class MessageServiceImpl extends ServiceImpl<MessageDao, Message> implements IMessageService {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private UsergroupDao usergroupDao;
    @Override
    public R getMessageCount(HttpServletRequest request){
        Integer uid = userService.CheckloginStatus(request);
        if(uid < 0){
            return R.success(1);
        }
        User user = userDao.selectById(uid);
        if (user == null) {
            return R.success(1);
        } else {
            LambdaQueryWrapper<Message> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Message::getUid,Integer.valueOf(uid)).or().eq(Message::getUid,10);
            Integer msgtotalcount = messageDao.selectCount(lqw);
            if (msgtotalcount != 0) {
                return R.success(msgtotalcount);
            } else {
                return R.success(1);
            }
        }
    }
    @Override
    public R getMessageList(HttpServletRequest request, Integer page){
        Integer uid = userService.CheckloginStatus(request);
        List<Message> emptyMessageList = new ArrayList<>();
        Message emptyMessage = new Message();
        R<List> r = new R<>();
        if(uid < 0){
            emptyMessage.setMsg("登陆状态错误,请重新登录");
            emptyMessageList.add(emptyMessage);
            r.setData(emptyMessageList);
            return r;
        }
        User user = userDao.selectById(uid);
        if (user == null) {
            emptyMessage.setMsg("登陆状态错误,请重新登录");
            emptyMessageList.add(emptyMessage);
            r.setData(emptyMessageList);
            return r;
        } else {
            if (page <= 0) {
                page = 1;
            }
            List<Message> messageList = messageDao.getMessageList(Integer.valueOf(uid), (page - 1) * 10);
            if (messageList != null && !messageList.isEmpty()) {
                r.setData(messageList);
                return r;
            } else {
                emptyMessage.setMsg("没有任何消息哦~");
                emptyMessageList.add(emptyMessage);
                r.setData(emptyMessageList);
                return r;
            }

        }
    }
    @Override
    public R sendpublicmsg(HttpServletRequest request, String publicmsg_content){
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
            if (usergroupDao.selectById(user.getUgid()).getLimits()>=200){
                messageDao.insertpublicmsg(publicmsg_content);
                return R.success("发布成功");
            }
            else {
                return R.success("用户权限不足");
            }
        }
    }
}
