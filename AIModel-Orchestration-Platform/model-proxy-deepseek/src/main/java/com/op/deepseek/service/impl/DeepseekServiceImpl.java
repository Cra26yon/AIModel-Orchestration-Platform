package com.op.deepseek.service.impl;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.op.api.client.UserClient;
import com.op.api.domain.vo.User_ApiKey_VO;
import com.op.common.domain.dto.UserLoginDTO;
import com.op.common.utils.UserHolder;
import com.op.deepseek.domain.DeepseekRequest;
import com.op.deepseek.service.DeepseekService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DeepseekServiceImpl implements DeepseekService {

    private final Gson gson = new Gson();

    @Autowired
    private UserClient userClient;

    @Override
    public String doChat(String question) throws IOException, UnirestException {
        Unirest.setTimeouts(0, 0);

        //DeepseekRequest: 自己的实体类名称
        List<DeepseekRequest.Message> messages = new ArrayList<>();
        /*//给deepSeek一个角色
        messages.add(DeepseekRequest.Message.builder().role("system").content("你是一个语言学家").build());*/
        // question：说你自己想说的话
        messages.add(DeepseekRequest.Message.builder().role("user").content(question).build());

        DeepseekRequest requestBody = DeepseekRequest.builder()
                .model("deepseek-chat")
                .messages(messages)
                .build();
        String apiKey = getApiKey();
        HttpResponse<String> response = Unirest.post("https://api.deepseek.com/chat/completions")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer "+ apiKey)
                .body(gson.toJson(requestBody))
                .asString();
        return  response.getBody();
    }

    private String getApiKey() {
        UserLoginDTO userDTO = UserHolder.getUser();
        Long userId = userDTO.getUserId();
        User_ApiKey_VO apiKeyVo = userClient.getApiKeyByUserId(userId);
        String deepseekApikey = apiKeyVo.getDeepseek_apikey();
        return deepseekApikey;
    }
}
