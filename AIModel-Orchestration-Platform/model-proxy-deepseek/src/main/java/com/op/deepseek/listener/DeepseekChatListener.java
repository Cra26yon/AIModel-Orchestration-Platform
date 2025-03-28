package com.op.deepseek.listener;

import com.op.deepseek.service.DeepseekService;
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
public class DeepseekChatListener {

    private final DeepseekService deepseekService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "op.deepseek.topic.queue", durable = "true"),
            exchange = @Exchange(name = "op.selectMultipleModel.topic.exchange", type = ExchangeTypes.TOPIC),
            key = {"#.deepseek.#"}
    ))
    public void listenDeepseekChat(String question) {
        log.info("deepseek队列接收到来自selectMultipleModel交换机的消息: {}", question);
        String response = null;
        try {
            response = deepseekService.doChat(question);
        } catch (Exception e) {
            response = "deepseek模型请求失败";
            log.error("deepseek模型请求失败", e);
            throw new RuntimeException(e);
        }
    }
}
