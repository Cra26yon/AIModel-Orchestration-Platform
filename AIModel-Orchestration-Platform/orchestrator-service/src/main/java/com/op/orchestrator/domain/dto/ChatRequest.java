package com.op.orchestrator.domain.dto;

import lombok.Data;

@Data
public class ChatRequest {
    /*
    * 选择的AI大模型
    * */
    private String model;
    /*
    * 提出的问题
    * */
    private String question;
}
