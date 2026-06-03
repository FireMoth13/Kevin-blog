package com.kevin.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kevin.blog.filter.JwtAuthenticationFilter;

//定义哪些路径需要认证、哪些放行、Session 策略、CSRF 策略、过滤器顺序。
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .csrf(csrf-> csrf.disable()) //禁用CSRF保护，因为我们使用JWT，不需要CSRF
            .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //设置Session为无状态，因为我们使用JWT，不需要Session
            //配置路径权限
            .authorizeHttpRequests(auth->auth
                .requestMatchers("/api/public/**").permitAll()      //公共接口无需认证,文章列表、文章详情等
                .requestMatchers("/api/admin/login").permitAll()    //登录接口无需认证。登录接口必须在管理员前面
                .requestMatchers("/api/admin/**").authenticated()   //管理员接口需要认证，增删改查
                .anyRequest().permitAll()                           //其他接口默认放行
            )
            .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class); //在UsernamePasswordAuthenticationFilter之前添加JwtAuthenticationFilter
        return http.build();
    }
}
