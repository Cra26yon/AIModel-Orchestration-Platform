package com.op.glm4.config;

import com.zhipu.oapi.ClientV4;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatGLM4Config {
    @Bean
    public ClientV4 clientV4() {
        return new ClientV4.Builder("46b1b0a4c4134a798c15377090379fd4.5hNBYYroPMaW5ag4").build();
    }
}
