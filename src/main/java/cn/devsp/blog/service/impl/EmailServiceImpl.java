package cn.devsp.blog.service.impl;

import cn.devsp.blog.common.R;
import cn.devsp.blog.dao.CaptchaDao;
import cn.devsp.blog.domain.Captcha;
import cn.devsp.blog.service.EmailService;
import cn.devsp.blog.util.CheckCodeUtil;
import cn.devsp.blog.util.CheckEmailFormatUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.time.LocalDateTime;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private CaptchaDao captchaDao;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //Spring Boot 提供了一个发送邮件的简单抽象，使用的是下面这个接口，这里直接注入即可使用
    @Autowired
    private JavaMailSender mailSender;

    // 配置文件中我的qq邮箱
    @Value("${spring.mail.from}")
    private String from;

    /**
     * 简单文本邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public R sendSimpleMail(String to, String subject, String content) {
        if (CheckEmailFormatUtil.isEmail(to)){
            Integer checksendtime = captchaDao.checksendtime(to);
            if (checksendtime==null)//检查距离上次发送验证码的时间,返回为null说明该邮箱从未发送过验证码,返回数字即为距离上次发送的时间间隔(秒数),300即为5分钟
            {
                String verifycode = CheckCodeUtil.generateVerifyCode(4);
                Captcha captcha = new Captcha();
                captcha.setEmail(to);
                captcha.setCaptcha(verifycode);
                captcha.setSendtime(LocalDateTime.now());
                captchaDao.insert(captcha);
                //创建SimpleMailMessage对象
                SimpleMailMessage message = new SimpleMailMessage();
                //邮件发送人
                message.setFrom(from);
                //邮件接收人
                message.setTo(to);
                //邮件主题
                message.setSubject(subject);
                //邮件内容
                message.setText(content+verifycode);
                //发送邮件
                mailSender.send(message);
                //验证码发送成功
                return R.success("验证码发送成功,如果未收到请检查垃圾邮件箱");
            }else{
                //间隔时间
                if (checksendtime>=60){
                    String verifycode = CheckCodeUtil.generateVerifyCode(4);
                    UpdateWrapper<Captcha> uw = new UpdateWrapper<>();
                    uw.eq("email",to).set("captcha",verifycode).set("sendtime",LocalDateTime.now());
                    captchaDao.update(null,uw);
                    //创建SimpleMailMessage对象
                    SimpleMailMessage message = new SimpleMailMessage();
                    //邮件发送人
                    message.setFrom(from);
                    //邮件接收人
                    message.setTo(to);
                    //邮件主题
                    message.setSubject(subject);
                    //邮件内容
                    message.setText(content+verifycode);
                    //发送邮件
                    mailSender.send(message);
                    //验证码发送成功
                    return R.success("验证码发送成功,如果未收到请检查垃圾邮件箱");
                }
                else {
                    return R.success("距离上次发送时间过短,请隔段时间后重试");
                }
            }
        }else {
            return R.success("邮箱格式不正确");
        }

    }

    /**
     * html邮件
     * @param to 收件人,多个时参数形式 ："xxx@xxx.com,xxx@xxx.com,xxx@xxx.com"
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        //获取MimeMessage对象
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true);
            //邮件发送人
            messageHelper.setFrom(from);
            //邮件接收人,设置多个收件人地址
            InternetAddress[] internetAddressTo = InternetAddress.parse(to);
            messageHelper.setTo(internetAddressTo);
            //messageHelper.setTo(to);
            //邮件主题
            message.setSubject(subject);
            //邮件内容，html格式
            messageHelper.setText(content, true);
            //发送
            mailSender.send(message);
            //日志信息
            logger.info("邮件已经发送。");
        } catch (Exception e) {
            logger.error("发送邮件时发生异常！", e);
        }
    }

    /**
     * 带附件的邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件
     */
    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource file = new FileSystemResource(new File(filePath));
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName, file);
            mailSender.send(message);
            //日志信息
            logger.info("邮件已经发送。");
        } catch (Exception e) {
            logger.error("发送邮件时发生异常！", e);
        }
    }
}
