-- 创建数据库
create database if not exists `open` default character set utf8mb4 collate utf8mb4_unicode_ci;
-- 创建所属用户
create user if not exists 'open135'@'%' IDENTIFIED BY 'open@2025';
-- 所属用户授权
grant all privileges on `open`.* to 'open135'@'%';
-- 刷新权限
flush privileges;

-- 表结构
create table if not exists `open`.`i18n_language`
(
    `id`          bigint(20)   not null comment '主键',
    `code`        varchar(32)  not null comment '语言代码',
    `name`        varchar(255) not null comment '语言名称',
    `icon`        blob         null comment '图标',
    `sort`        tinyint(5)   not null default 0 comment '排序',
    `remark`      varchar(500) null comment '备注',
    `region_flag` tinyint(1)   not null default 1 comment '区域标识【0=否,1=是】',
    `deleted`     bigint       not null default 0 comment '删除标识',
    `create_time` datetime     not null default current_timestamp comment '创建时间',
    `create_by`   bigint(20)   not null default 0 comment '创建者',
    `update_time` datetime     not null default current_timestamp comment '更新时间',
    `update_by`   bigint(20)   not null default 0 comment '更新者',
    `dept_id`     bigint(20)   not null default 0 comment '部门id',
    unique (`code`, `region_flag`, `deleted`),
    primary key (`id`)
) comment '语言表';

create table if not exists `open`.`i18n_key`
(
    `id`          bigint(20)   not null comment '主键',
    `code`        varchar(32)  not null comment '编码',
    `name`        varchar(255) not null comment '名称',
    `remark`      varchar(500) not null comment '备注',
    `deleted`     bigint       not null default 0 comment '删除标识',
    `create_time` datetime     not null default current_timestamp comment '创建时间',
    `create_by`   bigint(20)   not null default 0 comment '创建者',
    `update_time` datetime     not null default current_timestamp comment '更新时间',
    `update_by`   bigint(20)   not null default 0 comment '更新者',
    `dept_id`     bigint(20)   not null default 0 comment '部门id',
    primary key (`id`)
) comment '键表';

create table if not exists `open`.`i18n_value`
(
    `id`          bigint(20)   not null comment '主键',
    `key_id`      bigint(20)   not null comment '键id',
    `language_id` bigint(20)   not null comment '语言id',
    `content`     varchar(255)          default null comment '内容',
    `remark`      varchar(500) null comment '备注',
    `deleted`     bigint       not null default 0 comment '删除标识',
    `create_time` datetime     not null default current_timestamp comment '创建时间',
    `create_by`   bigint(20)   not null default 0 comment '创建者',
    `update_time` datetime     not null default current_timestamp comment '更新时间',
    `update_by`   bigint(20)   not null default 0 comment '更新者',
    `dept_id`     bigint(20)   not null default 0 comment '部门id',
    primary key (`id`)
) comment '值表';