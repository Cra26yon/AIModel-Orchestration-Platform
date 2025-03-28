package com.op.orchestrator.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleChatResponse {
    /*
     * 多个AI答复
     * */
    private Map<String, String> responses = new HashMap<>();
}
