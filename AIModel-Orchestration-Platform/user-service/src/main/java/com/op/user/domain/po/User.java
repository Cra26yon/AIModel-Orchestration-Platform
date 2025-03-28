package com.op.user.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /*
    * 用户id
    * */
    private Long id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 用户密码，加密存储
     */
    private String password;

    /**
     * 注册手机号
     */
    private String phone;

    /**
     * 用户创建时间
     */
    private LocalDateTime created_at;

    /**
     * 使用状态（1正常 2冻结）
     */
    private Integer status;

}
