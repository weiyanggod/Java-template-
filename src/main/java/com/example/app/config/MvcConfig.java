package com.example.app.config;
import com.example.app.utils.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    private LoginInterceptor loginInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册自己的拦截器,并设置拦截的请求路径
        // addPathPatterns为拦截此请求路径的请求
        // excludePathPatterns为不拦截此路径的请求
        registry.addInterceptor(loginInterceptor).addPathPatterns("/BFBidding/**").excludePathPatterns("/BFBidding/login").excludePathPatterns("/BFBidding/captcha");
    }
}
