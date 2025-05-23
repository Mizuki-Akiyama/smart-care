package com.example.demo.config;

import com.example.demo.intercepter.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthenticationInterceptor interceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:7980");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(interceptor)
                .addPathPatterns("/**") // 对所有请求生效，也可以指定特定的URL模式
                .excludePathPatterns("/user/**");// 排除不需要拦截的路径
    }

}
