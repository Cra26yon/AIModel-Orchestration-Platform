package com.op.glm4.service;

import com.zhipu.oapi.service.v4.model.ChatMessage;

public interface Glm4Service {
    String doChat(String question);
}
