package com.op.orchestrator.service;

import com.op.orchestrator.domain.dto.ChatRequest;
import com.op.orchestrator.domain.dto.MultipleChatRequest;
import com.op.orchestrator.domain.vo.ChatResponse;
import com.op.orchestrator.domain.vo.MultipleChatResponse;

public interface OrchestratorService {
    ChatResponse route(ChatRequest request);

    MultipleChatResponse multipleChat(MultipleChatRequest multipleRequest);
}
