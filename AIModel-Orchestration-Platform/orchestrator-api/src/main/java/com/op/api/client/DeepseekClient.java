package com.op.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "model-proxy-deepseek")
public interface DeepseekClient {

    @PostMapping("/deepseek/chat")
    String doChat(@RequestBody String question);

}
