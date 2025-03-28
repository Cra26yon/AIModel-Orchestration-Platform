package com.op.common.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.op.common.domain.dto.UserLoginDTO;
import com.op.common.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.op.common.utils.RedisConstants.LOGIN_USER_KEY;

public class UserTokenInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public UserTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取请求头中的token
        String token = request.getHeader("authorization");
        // 2.基于Token获取redis中的用户信息
        String key = LOGIN_USER_KEY + token;
        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
        // 3.判断用户是否存在
        if (userMap.isEmpty()) {
            // 不存在，拦截，返回401状态码
            response.setStatus(401);
            return false;
        }
        // 4.将查询到的HashMap数据转为UserLoginDTO对象
        UserLoginDTO userLoginDTO = BeanUtil.fillBeanWithMap(userMap, new UserLoginDTO(), false);
        // 5.保存用户信息到ThreadLocal
        UserHolder.saveUser(userLoginDTO);
        // 6.放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户信息，避免内存泄漏
        UserHolder.removeUser();
    }
}
