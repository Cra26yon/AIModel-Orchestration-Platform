package com.op.orchestrator.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicConfig {

    /*
    * 声明Topic类型的交换机
    * */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("op.selectMultipleModel.topic.exchange");
    }
}
