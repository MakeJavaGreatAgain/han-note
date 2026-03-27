-- 创建表
CREATE TABLE t_user
(
    id BIGSERIAL PRIMARY KEY,                                      -- 使用 BIGSERIAL 实现自增，且自带索引
    hannote_id   VARCHAR(15) NOT NULL,
    password       VARCHAR(64),
    nickname       VARCHAR(24) NOT NULL,
    avatar         VARCHAR(120),
    birthday       DATE,
    background_img VARCHAR(120),
    phone          VARCHAR(11) NOT NULL,
    sex            SMALLINT             DEFAULT 0,                 -- PG 习惯用 SMALLINT 代替 tinyint
    status         SMALLINT    NOT NULL DEFAULT 0,
    introduction   VARCHAR(100),
    create_time    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP, -- PG 使用 TIMESTAMP
    update_time    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted     BOOLEAN     NOT NULL DEFAULT FALSE,             -- PG 有原生布尔类型，比 bit(1) 更易用

    -- 约束命名规范
    CONSTRAINT uk_hannote_id UNIQUE (hannote_id),
    CONSTRAINT uk_phone UNIQUE (phone)
);

-- PostgreSQL 的注释需要独立语句执行
COMMENT ON TABLE t_user IS '用户表';
COMMENT ON COLUMN t_user.id IS '主键ID';
COMMENT ON COLUMN t_user.hannote_id IS '小憨书号(唯一凭证)';
COMMENT ON COLUMN t_user.password IS '密码';
COMMENT ON COLUMN t_user.nickname IS '昵称';
COMMENT ON COLUMN t_user.avatar IS '头像';
COMMENT ON COLUMN t_user.birthday IS '生日';
COMMENT ON COLUMN t_user.background_img IS '背景图';
COMMENT ON COLUMN t_user.phone IS '手机号';
COMMENT ON COLUMN t_user.sex IS '性别(0：女 1：男)';
COMMENT ON COLUMN t_user.status IS '状态(0：启用 1：禁用)';
COMMENT ON COLUMN t_user.introduction IS '个人简介';
COMMENT ON COLUMN t_user.create_time IS '创建时间';
COMMENT ON COLUMN t_user.update_time IS '更新时间';
COMMENT ON COLUMN t_user.is_deleted IS '逻辑删除(false：未删除 true：已删除)';