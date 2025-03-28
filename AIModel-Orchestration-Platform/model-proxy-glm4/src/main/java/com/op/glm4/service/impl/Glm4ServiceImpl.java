package com.op.glm4.service.impl;

import com.op.api.client.UserClient;
import com.op.api.domain.vo.User_ApiKey_VO;
import com.op.common.domain.dto.UserLoginDTO;
import com.op.common.utils.UserHolder;
import com.op.glm4.service.Glm4Service;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Glm4ServiceImpl implements Glm4Service {

    @Autowired
    private UserClient userClient;

    public String doChat(String question) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRole(ChatMessageRole.USER.value());
        chatMessage.setContent(question);
        List<ChatMessage> messages = new ArrayList<>();
        if (chatMessage == null) {
            throw new IllegalArgumentException("ChatMessage cannot be null, system error occurred.");
        }
        messages.add(chatMessage);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("glm-4-flash")
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .build();
        ClientV4 clientV4 = buildClientV4();
        ModelApiResponse invokeModelApiResp = clientV4.invokeModelApi(chatCompletionRequest);
        String result = invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString();
        log.info("ZhiPuAI Response: {}", result);
        return result;
    }

    private ClientV4 buildClientV4() {
        String glm4Apikey = getApiKey();
        ClientV4 clientV4 = new ClientV4();
        clientV4.getConfig().setApiKey(glm4Apikey);
        return clientV4;
    }

    private String getApiKey() {
        UserLoginDTO userDTO = UserHolder.getUser();
        Long userId = userDTO.getUserId();
        User_ApiKey_VO apiKeyVo = userClient.getApiKeyByUserId(userId);
        String glm4Apikey = apiKeyVo.getGlm4_apikey();
        return glm4Apikey;
    }

}
