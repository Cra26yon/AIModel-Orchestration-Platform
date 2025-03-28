package com.op.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.op.common.exception.BadRequestException;
import com.op.common.exception.ForbiddenException;
import com.op.user.domain.dto.LoginFormDTO;
import com.op.user.domain.dto.Result;
import com.op.user.domain.dto.UserLoginDTO;
import com.op.user.domain.po.User;
import com.op.user.domain.po.User_ApiKey;
import com.op.user.domain.vo.UserLoginVO;
import com.op.user.domain.vo.User_ApiKey_VO;
import com.op.user.mapper.UserMapper;
import com.op.user.service.UserService;
import com.op.user.utils.JwtTools;
import com.op.user.utils.RegexUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static com.op.user.utils.RedisConstants.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JwtTools jwtTools;

    /*
     * 发送手机验证码
     * */
    @Override
    public Result sendCode(String phone, HttpSession session) {
        //检验手机格式是否无效
        boolean phoneInvalid = RegexUtils.isPhoneInvalid(phone);
        if (phoneInvalid) {
            //若无效则返回错误信息
            return Result.fail("手机格式有误");
        }
        //若有效则生成验证码
        String code = RandomUtil.randomNumbers(6);
        //保存验证码到session，该验证码持续时间为2min
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
        //发送验证码
        log.debug("验证码发送成功，验证码为：{}", code);
        //返回ok
        return Result.ok();
    }

    /*
     * 用户登录
     * */
    @Override
    public Result login(LoginFormDTO loginDTO, HttpSession session) {
        // 1.数据校验
        String phone = loginDTO.getPhone();
        String password = loginDTO.getPassword();
        if (RegexUtils.isPhoneInvalid(phone)) {
            throw new BadRequestException("用户电话号码格式有误");
        }
        // 2.根据用户电话号码来进行查询
        User user = userMapper.selectUserByPhone(phone);
        if (user == null) {
            //用户不存在，创建新用户并保存
            user = creatUserWithPhone(phone);
        }
        // 3.校验是否禁用
        if (user.getStatus() == 2) {
            throw new ForbiddenException("用户被冻结");
        }
        //4.判断是根据验证码还是密码进行登录
        if (password == null) {
            //验证码登录，从Redis中获取验证码并校验
            String code = loginDTO.getCode();
            String cacheCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
            if (cacheCode == null || !cacheCode.equals(code)) {
                throw new ForbiddenException("验证码错误");
            }
        }else {
            //密码登录，校验密码
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadRequestException("密码错误");
            }
        }
        // 5.生成JWT令牌作为TOKEN
        Long userId = user.getId();
        String token = jwtTools.createJwt(userId);
        // 6.封装为DTO
        UserLoginDTO dto = new UserLoginDTO();
        dto.setUserId(user.getId());
        dto.setPhone(user.getPhone());
        // 7. 将用户信息保存到Redis中
        Map<String, Object> userMap = BeanUtil.beanToMap(dto);
        Map<String, String> cacheUserMap = new HashMap<>();
        userMap.forEach((k, v) -> {
            cacheUserMap.put(k, StrUtil.toString(v));
        });
        String tokenKey = LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, cacheUserMap);
        return Result.ok(token);
    }

    /*
    * 根据电话号码创建用户并保存
    * */
    public User creatUserWithPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        user.setUsername("用户" + phone.substring(0, 3) + "****" + phone.substring(7));
        user.setCreated_at(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        userMapper.insertUser(user);
        return user;
    }

    /*
     * 根据用户id获取用户apikey
     * */
    @Override
    public User_ApiKey_VO getApiKeyByUserId(Long userId) {
        User_ApiKey user_apiKey = userMapper.selectApiKeyByUserId(userId);
        // 检查 user_apiKey 是否为空
        if (user_apiKey == null) {
            throw new BadRequestException("用户API密钥不存在");
        }
        // 将结果封装为 VO 并返回
        User_ApiKey_VO apiKeyVo = new User_ApiKey_VO();
        apiKeyVo.setUser_id(user_apiKey.getUser_id());
        apiKeyVo.setGlm4_apikey(user_apiKey.getGlm4_apikey());
        apiKeyVo.setDeepseek_apikey(user_apiKey.getDeepseek_apikey());
        return apiKeyVo;
    }

}
