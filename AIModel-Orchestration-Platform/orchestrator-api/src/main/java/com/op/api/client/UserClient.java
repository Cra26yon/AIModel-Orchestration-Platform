package com.op.api.client;

import com.op.api.domain.vo.User_ApiKey_VO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "user-service")
public interface UserClient {

    @GetMapping("/user/apikey/{userId}")
    User_ApiKey_VO getApiKeyByUserId(@PathVariable("userId") Long userId);

}
