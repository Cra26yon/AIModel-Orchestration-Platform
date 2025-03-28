package com.op.orchestrator.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleChatRequest {
    /*
     * 选择的多个AI大模型
     * */
    private List<String> models = new ArrayList<>();
    /*
     * 提出的问题
     * */
    private String question;
}
