package cn.devsp.blog.service;

import cn.devsp.blog.common.R;
import cn.devsp.blog.domain.Collect;
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
public interface ICollectService extends IService<Collect> {

    R insertcollect(HttpServletRequest request, Integer pid);

    R deletecollection(HttpServletRequest request, Integer pid);

    R getusercollect(HttpServletRequest request, Integer page);

    R getcollectcount(HttpServletRequest request);
}
