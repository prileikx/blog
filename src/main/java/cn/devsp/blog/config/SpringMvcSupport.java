package cn.devsp.blog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Slf4j
@Configuration
public class SpringMvcSupport extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始进行静态资源映射...");
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/resource/**").addResourceLocations("/resource/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
        registry.addResourceHandler("/bootstrap-5.3.0-alpha1-dist/**").addResourceLocations("/bootstrap-5.3.0-alpha1-dist/");
        registry.addResourceHandler("/user/**").addResourceLocations("/user/");
        registry.addResourceHandler("/index.html").addResourceLocations("/index.html");
        registry.addResourceHandler("/search.html").addResourceLocations("/search.html");
        registry.addResourceHandler("/PosrList.html").addResourceLocations("/PosrList.html");
        registry.addResourceHandler("/postContent.html").addResourceLocations("/postContent.html");
        registry.addResourceHandler("/upload.html").addResourceLocations("/upload.html");
        registry.addResourceHandler("/header.html").addResourceLocations("/header.html");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("/favicon.ico");
    }
}