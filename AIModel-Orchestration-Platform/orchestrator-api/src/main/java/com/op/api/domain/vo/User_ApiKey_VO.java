package com.op.api.domain.vo;

import lombok.Data;

@Data
public class User_ApiKey_VO {
    private Long user_id;
    private String glm4_apikey;
    private String deepseek_apikey;
}
