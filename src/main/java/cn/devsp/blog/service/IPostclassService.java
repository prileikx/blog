package cn.devsp.blog.service;

import cn.devsp.blog.common.R;
import cn.devsp.blog.domain.Postclass;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author prileikx
 * @since 2023-12-03
 */
public interface IPostclassService extends IService<Postclass> {
    R selectpostname();
}
