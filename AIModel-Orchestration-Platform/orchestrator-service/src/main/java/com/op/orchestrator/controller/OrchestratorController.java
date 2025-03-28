package com.op.orchestrator.controller;

import com.op.orchestrator.domain.dto.ChatRequest;
import com.op.orchestrator.domain.dto.MultipleChatRequest;
import com.op.orchestrator.domain.vo.ChatResponse;
import com.op.orchestrator.domain.vo.MultipleChatResponse;
import com.op.orchestrator.service.OrchestratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/orchestrator")
public class OrchestratorController {

    @Autowired
    private OrchestratorService orchestratorService;

    /*
    * 将请求路由至指定的AI大模型，返回AI的回答
    * 接收到前端传来的ChatRequest对象，解析对象中包含包含的饿model属性(用于指定AI大模型)与question属性(用户输入的问题)
    * */
    @PostMapping("/chat")
    public ChatResponse route(@RequestBody ChatRequest request) {
        return orchestratorService.route(request);
    }

    /*
    * 同时请求多个模型并比较结果
    * */
    @PostMapping("/multipleChat")
    public MultipleChatResponse multipleChat(@RequestBody MultipleChatRequest multipleRequest) {
        return orchestratorService.multipleChat(multipleRequest);
    }
}
