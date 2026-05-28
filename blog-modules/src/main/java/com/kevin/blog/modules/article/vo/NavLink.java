package com.kevin.blog.modules.article.vo;

import lombok.AllArgsConstructor;  //全参构造器，方便测试和手动创建对象
import lombok.Data;
import lombok.NoArgsConstructor;   //无参构造器，方便反序列化

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NavLink {
    private String slug;
    private String title;
}
