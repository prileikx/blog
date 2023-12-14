package cn.devsp.blog.service;

import cn.devsp.blog.common.R;
import cn.devsp.blog.domain.Message;
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
public interface IMessageService extends IService<Message> {

    R getMessageCount(HttpServletRequest request);

    R getMessageList(HttpServletRequest request, Integer page);

    R sendpublicmsg(HttpServletRequest request, String publicmsg_content);
}
