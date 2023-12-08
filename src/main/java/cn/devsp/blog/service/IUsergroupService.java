package cn.devsp.blog.service;

import cn.devsp.blog.common.R;
import cn.devsp.blog.domain.Usergroup;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author prileikx
 * @since 2023-12-03
 */
public interface IUsergroupService extends IService<Usergroup> {

    R usergroup(String reuid);
}
