package com.kevin.blog.modules.admin.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kevin.blog.common.exception.BusinessException;
import com.kevin.blog.common.result.ResultCode;
import com.kevin.blog.common.util.JwtUtil;
import com.kevin.blog.modules.admin.entity.Admin;
import com.kevin.blog.modules.admin.mapper.AdminMapper;
import com.kevin.blog.modules.admin.service.AdminService;
import cn.hutool.crypto.digest.BCrypt;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper,Admin> implements AdminService {
    @Override
    public String login(String username, String password) {
        //1.查用户
        Admin admin=lambdaQuery().eq(Admin::getUsername,username).one();
        if(admin==null){
            throw new BusinessException(ResultCode.UNAUTHORIZED,"用户名或密码错误");
        }
        //2.校验密码
        if(!BCrypt.checkpw(password,admin.getPassword())){
            throw new BusinessException(ResultCode.UNAUTHORIZED,"用户名或密码错误");
        }
        //3.生成jwt
        return JwtUtil.generate(admin.getId());
    }
    
}
