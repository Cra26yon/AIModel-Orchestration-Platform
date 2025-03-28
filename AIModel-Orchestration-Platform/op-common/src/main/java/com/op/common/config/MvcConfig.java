package com.op.common.config;

import com.op.common.interceptor.UserTokenInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
@ConditionalOnClass(DispatcherServlet.class)
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    //registry指的是拦截器的注册器
    public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns方法用于指定需要拦截的请求路径，不使用该方法时默认为全部路径
        registry.addInterceptor(userTokenInterceptor());
    }

    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        // 通过构造函数注入 StringRedisTemplate
        return new UserTokenInterceptor(stringRedisTemplate);
    }
}
