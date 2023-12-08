package cn.devsp.blog.service.impl;

import cn.devsp.blog.domain.Captcha;
import cn.devsp.blog.dao.CaptchaDao;
import cn.devsp.blog.service.ICaptchaService;
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
public class CaptchaServiceImpl extends ServiceImpl<CaptchaDao, Captcha> implements ICaptchaService {

}
