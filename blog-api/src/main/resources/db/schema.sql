-- 管理员
DROP TABLE IF EXISTS blog_article_tag;
DROP TABLE IF EXISTS game_record;
DROP TABLE IF EXISTS blog_article;
DROP TABLE IF EXISTS blog_category;
DROP TABLE IF EXISTS blog_tag;
DROP TABLE IF EXISTS blog_admin;

CREATE TABLE blog_admin (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    nickname    VARCHAR(50),
    avatar      VARCHAR(255),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 文章
CREATE TABLE blog_article (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(200)  NOT NULL,
    slug         VARCHAR(200)  NOT NULL UNIQUE,
    summary      VARCHAR(500),
    content_md   LONGTEXT,
    content_html LONGTEXT,
    cover        VARCHAR(500),
    category_id  BIGINT,
    status       TINYINT  DEFAULT 0 COMMENT '0=草稿,1=已发布',
    view_count   INT      DEFAULT 0,
    is_top       TINYINT  DEFAULT 0 COMMENT '0=否,1=置顶',
    create_time  DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 分类
CREATE TABLE blog_category (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    slug        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200),
    sort        INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 标签
CREATE TABLE blog_tag (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    slug VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 文章-标签 中间表
CREATE TABLE blog_article_tag (
    article_id BIGINT NOT NULL,
    tag_id     BIGINT NOT NULL,
    PRIMARY KEY (article_id, tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 游戏记录（预留）
CREATE TABLE game_record (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    game_type  VARCHAR(50)  NOT NULL,
    user_key   VARCHAR(100) NOT NULL COMMENT '游客标识',
    score      INT DEFAULT 0,
    extra_data JSON,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO blog_admin (username, password, nickname)
VALUES ('admin', '暂时填明文,后面改成BCrypt加密后的值', 'Kevin');