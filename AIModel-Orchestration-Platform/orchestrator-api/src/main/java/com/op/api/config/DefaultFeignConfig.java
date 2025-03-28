package com.op.api.config;

import com.op.common.utils.UserHolder;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class DefaultFeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                Long userId = UserHolder.getUser().getUserId();
                if (userId != null) {
                    requestTemplate.header("userId", userId.toString());
                }
            }
        };
    }

    /*@Bean
    public ItemClientFallback itemClientFallback() {
        return new ItemClientFallback();
    }*/
}
