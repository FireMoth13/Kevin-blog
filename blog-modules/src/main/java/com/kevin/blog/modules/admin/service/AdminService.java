package com.kevin.blog.modules.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kevin.blog.modules.admin.entity.Admin;

public interface AdminService extends IService<Admin> {
    Long login(String username, String password);
}
