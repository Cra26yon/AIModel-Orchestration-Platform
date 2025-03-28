package com.op.glm4.listener;

import com.op.glm4.service.Glm4Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Glm4ChatListener {

    private final Glm4Service glm4Service;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "op.glm4.topic.queue", durable = "true"),
            exchange = @Exchange(name = "op.selectMultipleModel.topic.exchange", type = ExchangeTypes.TOPIC),
            key = {"#.glm4.#"}
    ))
    public void listenGlm4TopicQueue(String question) {
        log.info("glm4队列接收到来自selectMultipleModel交换机的消息: {}", question);
        String response = null;
        try {
            response = glm4Service.doChat(question);
        } catch (Exception e) {
            response = "glm4模型请求失败";
            log.error("glm4模型请求失败", e);
            throw new RuntimeException(e);
        }
    }
}
