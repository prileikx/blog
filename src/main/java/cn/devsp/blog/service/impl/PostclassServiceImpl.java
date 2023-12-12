package cn.devsp.blog.service.impl;

import cn.devsp.blog.common.R;
import cn.devsp.blog.domain.Postclass;
import cn.devsp.blog.dao.PostclassDao;
import cn.devsp.blog.service.IPostclassService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class PostclassServiceImpl extends ServiceImpl<PostclassDao, Postclass> implements IPostclassService {
    @Autowired
    private PostclassDao postclassDao;
    @Override
    public R selectpostname() {
        List<Postclass> postClass = postclassDao.selectList(null);
        return R.success(JSON.toJSONString(postClass));
    }
    @Override
    public R getpostclassname(String postclass){
        LambdaQueryWrapper<Postclass> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Postclass::getEnglishname,postclass);
        Postclass pc = postclassDao.selectOne(lqw);
        R r = new R<Postclass>();
        r.setData(pc);
        return r;
    }
}
