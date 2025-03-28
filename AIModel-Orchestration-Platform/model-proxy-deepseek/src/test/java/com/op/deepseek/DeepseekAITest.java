package com.op.deepseek;

import com.op.deepseek.service.impl.DeepseekServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class DeepseekAITest {
    @Resource
    private DeepseekServiceImpl deepseekServiceImpl;

    @Test
    public void testDeepseekAI() {
        String response = null;
        try {
            response = deepseekServiceImpl.doChat("请你为我介绍一下广东工业大学");
        } catch (Exception e) {
            throw new RuntimeException("第三方服务异常", e);
        }
        System.out.println(response);
    }
}
