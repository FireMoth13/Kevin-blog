package com.kevin.blog.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kevin.blog.config.JwtUtil;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.ServletException;
import java.util.Collections;

//用于拦截需要认证的请求，验证 JWT 的有效性，并将用户信息存储在 SecurityContext 中，以供后续的授权检查使用。
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {  //继承 OncePerRequestFilter，保证每个请求只被过滤一次
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws ServletException,IOException{
        //1.从请求头里拿token
        String token=jwtUtil.getTokenFromRequest(request);
        if(token!=null){
            try{
                //2.解析token，拿到adminId
                Long adminId=jwtUtil.parseToken(token);
                //3.构造认证对象
                UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(adminId,null, Collections.emptyList());
                //4.写入安全上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                //Token无效或过期，不设认证，后续SecurityConfig的规则决定是否放行
            }
        }
        //5.无论有没有token，都放行，继续过滤器链
        chain.doFilter(request, response);
    }
}
