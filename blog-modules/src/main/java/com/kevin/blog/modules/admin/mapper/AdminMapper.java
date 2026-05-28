package com.kevin.blog.modules.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kevin.blog.modules.admin.entity.Admin;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
