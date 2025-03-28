package com.op.common.utils;

import com.op.common.domain.dto.UserLoginDTO;
import org.springframework.stereotype.Component;

@Component
public class UserHolder {
    private static final ThreadLocal<UserLoginDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserLoginDTO user){
        tl.set(user);
    }

    public static UserLoginDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
