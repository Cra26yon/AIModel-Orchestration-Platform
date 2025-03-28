package com.op.user.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTools {
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
}
