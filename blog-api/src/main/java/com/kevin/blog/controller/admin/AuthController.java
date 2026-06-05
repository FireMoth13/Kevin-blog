package com.kevin.blog.controller.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevin.blog.common.result.Result;
import com.kevin.blog.config.JwtUtil;
import com.kevin.blog.modules.admin.entity.Admin;
import com.kevin.blog.modules.admin.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AuthController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        Long adminId = adminService.login(username, password);
        String token = jwtUtil.generate(adminId);
        return Result.success(Map.of("token", token));
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> info() {
        Long adminId = (Long) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Admin admin = adminService.getById(adminId);
        return Result.success(Map.of(
                "id", admin.getId(),
                "username", admin.getUsername(),
                "nickname", admin.getNickname(),
                "avatar", admin.getAvatar()
        ));
    }
}