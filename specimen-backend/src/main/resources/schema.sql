-- 创建数据库
CREATE DATABASE IF NOT EXISTS specimen_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE specimen_db;

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
  `password` VARCHAR(128) NOT NULL COMMENT '密码（BCrypt加密）',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `email` VARCHAR(100) COMMENT '邮箱',
  `role` VARCHAR(20) DEFAULT 'user' COMMENT '角色: user/admin',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 1启用 0禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_username (`username`),
  INDEX idx_role (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 植物标本表
CREATE TABLE IF NOT EXISTS `specimen` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标本ID',
  `specimen_no` VARCHAR(32) UNIQUE NOT NULL COMMENT '标本编号',
  `name` VARCHAR(100) NOT NULL COMMENT '植物名称',
  `latin_name` VARCHAR(100) COMMENT '拉丁名',
  `family` VARCHAR(50) COMMENT '科名',
  `genus` VARCHAR(50) COMMENT '属名',
  `collector_id` BIGINT COMMENT '采集人ID',
  `collection_date` DATE COMMENT '采集日期',
  `location` VARCHAR(200) COMMENT '采集地点',
  `longitude` DECIMAL(10,6) COMMENT '经度',
  `latitude` DECIMAL(10,6) COMMENT '纬度',
  `image_url` VARCHAR(255) COMMENT '图片URL',
  `description` TEXT COMMENT '描述',
  `status` VARCHAR(20) DEFAULT '已入库' COMMENT '状态',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_specimen_no (`specimen_no`),
  INDEX idx_name (`name`),
  INDEX idx_family (`family`),
  INDEX idx_genus (`genus`),
  INDEX idx_collector (`collector_id`),
  INDEX idx_location (`longitude`, `latitude`),
  FOREIGN KEY (`collector_id`) REFERENCES `user`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='植物标本表';

-- 3. 采集记录表
CREATE TABLE IF NOT EXISTS `collection_record` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `specimen_name` VARCHAR(100) COMMENT '标本名称',
  `collection_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '采集时间',
  `longitude` DECIMAL(10,6) COMMENT '经度',
  `latitude` DECIMAL(10,6) COMMENT '纬度',
  `remarks` TEXT COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX idx_user (`user_id`),
  INDEX idx_time (`collection_time`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采集记录表';

-- 4. 地图标注表
CREATE TABLE IF NOT EXISTS `map_marker` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标注ID',
  `specimen_id` BIGINT COMMENT '标本ID',
  `longitude` DECIMAL(10,6) NOT NULL COMMENT '经度',
  `latitude` DECIMAL(10,6) NOT NULL COMMENT '纬度',
  `label` VARCHAR(100) COMMENT '标签',
  `marker_color` VARCHAR(20) DEFAULT '#4CAF50' COMMENT '标记颜色',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  INDEX idx_location (`longitude`, `latitude`),
  INDEX idx_specimen (`specimen_id`),
  FOREIGN KEY (`specimen_id`) REFERENCES `specimen`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地图标注表';

-- 5. 公告表
CREATE TABLE IF NOT EXISTS `announcement` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID',
  `title` VARCHAR(100) NOT NULL COMMENT '标题',
  `content` TEXT COMMENT '内容',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_create_time (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- 插入默认管理员账号 (密码: admin123, 使用BCrypt加密)
INSERT INTO `user` (`username`, `password`, `real_name`, `email`, `role`)
VALUES ('admin', '$2a$10$YJZBvzQgvVmT0VFxC6YZkeQlVmVXlMKZjhCXZ3DVH7k0d5QOE3kEO', '系统管理员', 'admin@specimen.com', 'admin');

-- 插入测试用户 (密码: user123)
INSERT INTO `user` (`username`, `password`, `real_name`, `email`, `role`)
VALUES ('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EXcxv5aVQJA9T6BXVYxUdS', '测试用户', 'user@specimen.com', 'user');

-- 插入示例公告
INSERT INTO `announcement` (`title`, `content`)
VALUES ('欢迎使用植物标本库管理系统', '本系统支持标本采集、入库、检索和数字地图展示功能。');
