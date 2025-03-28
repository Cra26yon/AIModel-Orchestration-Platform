package com.op.user.controller;

import com.op.user.domain.dto.LoginFormDTO;
import com.op.user.domain.dto.Result;
import com.op.user.domain.vo.UserLoginVO;
import com.op.user.domain.vo.User_ApiKey_VO;
import com.op.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送手机验证码
     */
    @PostMapping("/code")
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session) {
        // 发送短信验证码并保存验证码
        return userService.sendCode(phone, session);
    }

    /*
    * 用户登录
    * */
    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginFormDTO, HttpSession session){
        return userService.login(loginFormDTO, session);
    }

    /*
    * 根据用户id获取用户apikey
    * */
    @GetMapping("/apikey/{userId}")
    public User_ApiKey_VO getApiKeyByUserId(@PathVariable("userId") Long userId){
        return userService.getApiKeyByUserId(userId);
    }

}
