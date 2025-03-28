package com.op.glm4;

import com.op.glm4.service.impl.Glm4ServiceImpl;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class ZhipuAITest {
    @Resource
    private Glm4ServiceImpl glm4ServiceImpl;

    @Test
    public void testZhiPuAI() {
        String response = null;
        try {
            response = glm4ServiceImpl.doChat("请你为我介绍一下广东工业大学");
        } catch (Exception e) {
            throw new RuntimeException("第三方服务异常", e);
        }
        System.out.println(response);
    }
}
