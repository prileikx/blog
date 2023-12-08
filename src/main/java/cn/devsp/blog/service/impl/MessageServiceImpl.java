package cn.devsp.blog.service.impl;

import cn.devsp.blog.domain.Message;
import cn.devsp.blog.dao.MessageDao;
import cn.devsp.blog.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
