package com.kevin.blog.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {
    @Value("${blog.jwt.secret}")
    private String secret;

    @Value("${blog.jwt.expiration}")
    private long expiration;  //过期时间

    @Value("${blog.jwt.header}")
    private String header;      

    @Value("${blog.jwt.tokenPrefix}")
    private String tokenPrefix;    //token前缀，通常是"Bearer "

    public String generate(Long adminId){  //生成token
        SecretKey key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));//把 application.yml 里的字符串转成字节数组，再用 Keys.hmacShaKeyFor() 转成 JWT 能用的 SecretKey 对象。
        return Jwts.builder()
                .subject(adminId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(key)  //使用密钥签名
                .compact();  //压缩成字符串
    }

    public Long parseToken(String token){  //解析token
        SecretKey key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        Claims claims=Jwts.parser()         //创建解析器
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)   //解析token，验证签名
                .getPayload();         //拿到载荷
        
        return Long.valueOf(claims.getSubject());  //subject里存的是adminId
    }

    public String getTokenFromRequest(HttpServletRequest request){  //从请求头里拿token
        String bearerToken=request.getHeader(header);
        if(bearerToken!=null && bearerToken.startsWith(tokenPrefix)){
            return bearerToken.substring(tokenPrefix.length());  //substring(7) 表示从第 7 个字符开始取到末尾。
        }
        return null;
    }
    
}
