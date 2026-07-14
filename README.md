# Kevin's Blog

基于 Spring Boot 3.3.5 + Vue 3 的个人博客系统，前后端分离，动漫二次元风格。

**线上地址**：[http://124.221.139.246](http://124.221.139.246)  
**域名**：kevinblog.online（备案中，备案完成后升级 HTTPS）
**服务器**：腾讯云 2核2G 3Mbps · 上海 · Ubuntu 22.04 LTS

## 技术栈

### 后端
| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 17 | — |
| Spring Boot | 3.3.5 | Web 框架、自动配置 |
| MyBatis-Plus | 3.5.7 | ORM、分页 |
| MySQL | 8.0 | 数据存储 |
| Redis | 7.x | 缓存、排行榜预留 |
| Spring Security + jjwt | 0.12.6 | JWT 无状态认证 |
| Flexmark | 0.64.8 | Markdown → HTML |
| Hutool | 5.8.29 | BCrypt、拼音、Bean 拷贝 |
| Maven | 多模块 | blog-common / blog-modules / blog-api |

### 前端
| 技术 | 版本 | 用途 |
|------|------|------|
| Vue 3 | Composition API + `<script setup>` | 组件化 |
| Vite | 5 | 构建、HMR |
| vue-router | 4 | 路由、导航守卫 |
| axios | — | HTTP |
| marked | — | Markdown 客户端渲染 |
| CSS | 纯手写 | 日夜双主题、毛玻璃、霓虹发光 |

### 部署
| 组件 | 用途 |
|------|------|
| Ubuntu 22.04 LTS | 服务器系统 |
| Nginx | 反向代理、静态文件、SSL |
| Let's Encrypt / 腾讯云 SSL | HTTPS |
| systemd | 后端服务管理 |

---

## 架构

```
nginx (443/80)
  ├─ /api/*  ──→  Spring Boot :8080
  │                ├── SecurityConfig  (JWT 过滤器链)
  │                ├── AuthController  (登录 / 管理员信息)
  │                ├── ArticleController (CRUD + 公开列表/详情/归档)
  │                ├── CategoryController
  │                └── TagController
  │
  └─ /*  ──→  /var/www/blog/dist  (Vue SPA 静态文件)
```

项目多模块结构：

```
kevin-blog/
├── blog-common/        公共模块（Result、异常、BCrypt 工具）
├── blog-modules/       业务模块（Entity、Mapper、Service、DTO/VO）
├── blog-api/           启动模块（Controller、Security、JWT 配置）
├── blog-frontend/      Vue 3 前端
└── pom.xml             父 POM
```

---

## 功能

### 公开端
- 首页：霓虹时钟 + 置顶文章 + 最新文章 + 日夜切换
- 文章列表：搜索、分类筛选、标签筛选、分页
- 文章详情：Markdown 渲染、上下篇导航、浏览量统计
- 归档：按年月分组
- 404 友好页
- 游戏入口（预留）

### 管理端
- 登录/退出、JWT 鉴权、路由守卫
- 文章 CRUD：Markdown 编辑器 + 实时预览
- 分类管理、标签管理（弹窗表单）
- 概览仪表盘

### 视觉
- 日夜双主题（右上角一键切换，localStorage 持久化）
- 静态图片背景 + Ken Burns 缓慢放大动画
- 毛玻璃卡片（`backdrop-filter: blur`）
- 霓虹发光文字（`text-shadow` 多层叠加）
- 暗色滚动条
- 响应式适配（桌面 / 平板 / 手机）

---

## 数据库

6 张表：

| 表 | 说明 |
|----|------|
| blog_admin | 管理员（单账号） |
| blog_article | 文章 |
| blog_category | 分类（一对多文章） |
| blog_tag | 标签（多对多文章） |
| blog_article_tag | 文章-标签中间表 |
| game_record | 游戏记录（预留） |

SQL 文件：`blog-api/src/main/resources/db/schema.sql`

---

## 本地开发

### 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 7.x
- Node.js 18+
- Maven 3.8+

### 启动后端

```bash
# 1. 创建数据库并执行建表
mysql -u root -p < blog-api/src/main/resources/db/schema.sql

# 2. 启动 Spring Boot（确保 MySQL 和 Redis 已运行）
mvn -f blog-api spring-boot:run
```

后端运行在 `http://localhost:8080`。

### 启动前端

```bash
cd blog-frontend
npm install
npm run dev
```

前端运行在 `http://localhost:5173`，请求自动代理到后端。

### API 测试

```bash
curl http://localhost:8080/api/public/articles
```

管理端登录：

```bash
curl -X POST http://localhost:8080/api/admin/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

---

## 生产部署

### 1. 构建后端

```bash
mvn clean package -DskipTests
```

产物：`blog-api/target/blog-api-1.0.0.jar`

### 2. 构建前端

```bash
cd blog-frontend
npm run build
```

产物：`blog-frontend/dist/`

### 3. 服务器环境

```bash
# 安装 JDK 17
sudo apt install openjdk-17-jdk -y

# 安装 MySQL 8.0
sudo apt install mysql-server -y

# 安装 Redis
sudo apt install redis-server -y

# 安装 Nginx
sudo apt install nginx -y
```

### 4. 配置 systemd 服务

```ini
# /etc/systemd/system/blog-api.service
[Unit]
Description=Kevin Blog API
After=network.target mysql.service redis.service

[Service]
User=ubuntu
WorkingDirectory=/home/ubuntu
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod -Dspring.config.additional-location=file:/home/ubuntu/blog-config/ /home/ubuntu/blog-api.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

### 5. 配置 Nginx

```nginx
server {
    listen 80;
    server_name example.com;

    root /var/www/blog/dist;
    index index.html;

    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location / {
        try_files $uri $uri/ /index.html;
    }
}
```

### 6. HTTPS（备案完成后）

```bash
sudo snap install --classic certbot
sudo certbot --nginx -d kevinblog.online
```

---

## 常用管理命令

```bash
# 重启后端
sudo systemctl restart blog-api

# 查看后端日志
sudo journalctl -u blog-api -f

# 前端更新（上传 dist 后）
sudo chown -R www-data:www-data /var/www/blog
sudo systemctl reload nginx

# SSL 自动续期测试
sudo certbot renew --dry-run
```

---

## License

MIT
