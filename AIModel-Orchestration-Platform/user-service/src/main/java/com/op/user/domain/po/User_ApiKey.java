package com.op.user.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User_ApiKey {

    /*
     * 用户id
     * */
    private Long user_id;

    /*
    * 质谱AI的apikey
    * */
    private String glm4_apikey;

    /*
    * deepseek AI的apikey
    * */
    private String deepseek_apikey;
}
