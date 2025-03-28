package com.op.orchestrator.service.impl;

import com.op.api.client.DeepseekClient;
import com.op.api.client.Glm4Client;
import com.op.common.exception.BadRequestException;
import com.op.orchestrator.domain.dto.ChatRequest;
import com.op.orchestrator.domain.dto.MultipleChatRequest;
import com.op.orchestrator.domain.vo.ChatResponse;
import com.op.orchestrator.domain.vo.MultipleChatResponse;
import com.op.orchestrator.enums.ModelType;
import com.op.orchestrator.service.OrchestratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class OrchestratorServiceImpl implements OrchestratorService {

    @Autowired
    private Glm4Client glm4Client;

    @Autowired
    private DeepseekClient deepseekClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /*
    * 将请求路由至指定的AI大模型，返回AI的回答
    * */
    @Override
    public ChatResponse route(ChatRequest request) {
        String model = request.getModel();
        String question = request.getQuestion();
        String response;
        if (ModelType.GLM4.equals(model)) {
            response = glm4Client.doChat(question);
        }else if (ModelType.DEEPSEEK.equals(model)) {
            response = deepseekClient.doChat(question);
        }else {
            throw new BadRequestException("AI大模型选择有误，请重试");
        }
        return new ChatResponse(response);
    }

    /*
     * 同时请求多个模型并比较结果
     * */
    @Override
    public MultipleChatResponse multipleChat(MultipleChatRequest multipleRequest) {
        List<String> models = multipleRequest.getModels();
        String question = multipleRequest.getQuestion();
        MultipleChatResponse mcs = new MultipleChatResponse();
        Map<String, String> responses = mcs.getResponses();
        for (int i = 0; i < models.size(); i++) {
            String model = models.get(i);
            String response = doChatForModel(model, question);
            responses.put(model, response);
        }
        if (responses == null) {
            throw new BadRequestException("AI大模型选择有误，请重试");
        }
        return new MultipleChatResponse(responses);
    }

    /*
    * 将问题路由给指定的AI大模型，返回AI的回答
    * */
    private String doChatForModel(String model, String question) {
        String response;
        if (ModelType.GLM4.equals(model)) {
            rabbitTemplate.convertAndSend("op.selectMultipleModel.topic.exchange", "op.glm4", question);
        } else if (ModelType.DEEPSEEK.equals(model)) {
            rabbitTemplate.convertAndSend("op.selectMultipleModel.topic.exchange", "op.deepseek", question);
        }else {
            throw new BadRequestException("AI大模型选择有误，请重试");
        }
        return response;
    }

    /*
     *
     *
     */
    private
}
