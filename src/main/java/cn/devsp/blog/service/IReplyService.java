package cn.devsp.blog.service;

import cn.devsp.blog.common.R;
import cn.devsp.blog.domain.Reply;
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
public interface IReplyService extends IService<Reply> {

    R replycount(String pid, HttpServletRequest request);

    R getReply(Integer page, HttpServletRequest request, Integer pid);

    R sendreply(HttpServletRequest request, Integer pid, String content, Integer replyuid);

    R deletereply(HttpServletRequest request, Integer rid);
}
