package com.op.user.service;

import com.op.user.domain.dto.LoginFormDTO;
import com.op.user.domain.dto.Result;
import com.op.user.domain.vo.UserLoginVO;
import com.op.user.domain.vo.User_ApiKey_VO;

import javax.servlet.http.HttpSession;

public interface UserService {

    /*
     * 发送手机验证码
     * */
    Result sendCode(String phone, HttpSession session);

    /*
    * 用户登录
    * */
    Result login(LoginFormDTO loginFormDTO, HttpSession session);

    /*
     * 根据用户id获取用户apikey
     * */
    User_ApiKey_VO getApiKeyByUserId(Long userId);

}
