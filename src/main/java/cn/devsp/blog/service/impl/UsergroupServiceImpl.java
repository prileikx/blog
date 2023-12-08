package cn.devsp.blog.service.impl;

import cn.devsp.blog.common.R;
import cn.devsp.blog.dao.UserDao;
import cn.devsp.blog.domain.Usergroup;
import cn.devsp.blog.dao.UsergroupDao;
import cn.devsp.blog.service.IUsergroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UsergroupServiceImpl extends ServiceImpl<UsergroupDao, Usergroup> implements IUsergroupService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UsergroupDao usergroupDao;
    @Override
    public R usergroup(String reuid){
        Integer ugid = userDao.selectById(reuid).getUgid();
        Usergroup userGroup = usergroupDao.selectById(ugid);
        R r = new R<Usergroup>();
        r.setData(userGroup);
        return r;
    }
}
