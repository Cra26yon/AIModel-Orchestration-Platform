package com.op.user.domain.dto;

import lombok.Data;

@Data
public class LoginFormDTO {
    /*
     * 用户手机号
     * */
    private String phone;
    /*
     * 验证码
     * */
    private String code;
    /*
     * 用户密码
     * */
    private String password;
}
