package cn.devsp.blog.controller;


import cn.devsp.blog.common.R;
import cn.devsp.blog.dao.UserDao;
import cn.devsp.blog.domain.User;
import cn.devsp.blog.service.impl.UserServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class UploadController extends HttpServlet {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserDao userDao;

    //实现的文件上传方法
    @PostMapping("/fileupload")
    public R upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Integer uid = userService.CheckloginStatus(request);
        if (uid < 0) {
            R.error("{\"msg\":\"用户验证错误,请重新登陆\"}");
        } else {
            User user = userDao.selectById(uid);
            if (user != null) {
                if (file.isEmpty()) {
                    return R.error("上传失败，请选择文件");
                }
                String fileName = file.getOriginalFilename();
                //创建上传文件的保存路径,建议在WEB-INF路径下,安全,用户无法直接访问上传的文件.

                String uploadPath = this.getClass().getResource("/").getPath().replaceFirst("/", "")+"META-INF/resources/resource/img/upload/";
                System.out.println("路径:"+uploadPath);
                //可以使用UUID(唯一标识的通用码),保证文件名唯一
                String uuidPath = UUID.randomUUID().toString();//生成一共随机的uuid
                //==========================创建存放目录========================//
                String realPath = uploadPath + "/" + uuidPath;
                //截取后缀名
                String fileExtName = fileName.substring(fileName.lastIndexOf(".") + 1);//从最后一个.后开始截取

                userDao.uploadimg(uuidPath + "." + fileExtName, Integer.valueOf(uid));
//                String filePath = "D:/photo/person/temp/";
                File dest = new File(uploadPath + uuidPath + "." + fileExtName);
                System.out.println("文件名:"+uploadPath + uuidPath + "." + fileExtName);
                try {
                    file.transferTo(dest);
                    System.out.println("上传成功");
                    return R.success("上传成功");
                } catch (IOException e) {
                    return R.error("上传失败！");
                }


            }
            else {
                return R.error("{\"msg\":\"用户验证错误,请重新登陆\"}");
            }
        }
        return R.error("未知错误");
    }
}
