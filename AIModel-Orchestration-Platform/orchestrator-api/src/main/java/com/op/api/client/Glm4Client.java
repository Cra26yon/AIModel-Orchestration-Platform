package com.op.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "model-proxy-glm4")
public interface Glm4Client {

    @PostMapping("/glm4/chat")
    String doChat(@RequestBody String question);
}
