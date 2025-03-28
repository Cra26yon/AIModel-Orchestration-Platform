package com.op.gateway.utils;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import com.op.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTools {

    /*
    * 生成JWT令牌
    * */
    public String createJwt(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userID", userId);
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, "AImodel-op")  //签名算法
                .setClaims(claims)   //自定义内容
                //有参构造方法
                .setExpiration(new Date(System.currentTimeMillis()+432000))  //令牌过期时间
                .compact();
        return token;
    }

    /*
    * 解析JWT令牌
    * */
    public Long parseToken(String token) {
        Claims claims=Jwts.parser()
                .setSigningKey("AImodel-op")
                //写入你刚才运行出来的jwt令牌
                .parseClaimsJws(token)
                .getBody();
        Long userID = (Long) claims.get("userID");
        return userID;
    }

}
