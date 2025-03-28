package com.op.user.domain.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private Long userId;
    private String phone;
}
