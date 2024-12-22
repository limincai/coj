# 数据库初始化

-- 创建库
create database if not exists ikuncode;

-- 切换库
use ikuncode;

-- 用户表
create table user
(
    user_id         bigint                                 not null comment '用户 id'
        primary key,
    user_role       tinyint      default 2                 not null comment '用户角色（0 -超级 管理员；1  -  管理员；2 - 普通用户）',
    user_account    varchar(128)                           not null comment '用户账号',
    user_password   varchar(128)                           not null comment '用户密码',
    user_email      varchar(128)                           not null comment '用户邮箱',
    user_nickname   varchar(128)                           not null comment '用户昵称',
    user_profile    varchar(512) default '快乐的ikun'      null comment '用户简介',
    user_avatar_url varchar(255)                           null comment '用户头像地址',
    user_jijiao     int          default 0                 not null comment '用户的鸡脚数量，用于排名',
    create_time     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted      tinyint      default 0                 not null comment '是否删除（0-否；1- 删除）',
    constraint user_account
        unique (user_account, user_email)
)
    comment '用户表';


-- 题目表
create table question
(
    question_id           bigint auto_increment comment '题目id'
        primary key,
    question_title        varchar(512)                       null comment '题目标题',
    question_description  text                               null comment '题目描述',
    question_tags         varchar(1024)                      null comment '题目标签 json 字符串',
    question_answer       text                               null comment '题目答案',
    question_submit_num   bigint   default 0                 not null comment '题目提交数量',
    question_accepted_num bigint   default 0                 not null comment '题目通过数量',
    question_judge_case   text                               null comment '判题用例 json 对象',
    question_judge_config text                               null comment '判题配置 json 对象',
    create_time           datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time           datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted            tinyint  default 0                 not null comment '是否删除，逻辑删除 0-未删除；1-删除'
)
    comment '题目表';

create index idx_question_id
    on question (question_id)
    comment '题目 id 索引';

create index idx_question_title
    on question (question_title)
    comment '题目标题索引';


-- 题目表
create table if not exists question
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(256)                       null comment '标题',
    content    text                               null comment '内容',
    tags       varchar(1024)                      null comment '标签列表（json 数组）',
    answer     text                               null comment '推荐答案',
    userId     bigint                             not null comment '创建用户 id',
    editTime   datetime default CURRENT_TIMESTAMP not null comment '编辑时间',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_title (title),
    index idx_userId (userId)
) comment '题目' collate = utf8mb4_unicode_ci;

-- 题目提交表
create table question_submit
(
    question_submit_id  bigint                             not null comment '题目提交id '
        primary key,
    user_id             bigint                             not null comment '题目提交关联的userId',
    question_id         bigint                             not null comment '题目提交关联 questionId',
    language            varchar(128)                       not null comment '提交代码语言',
    code                text                               not null comment '提交代码',
    question_judge_info text                               null comment '判题信息，json 对象',
    status              tinyint  default 0                 null comment '判题状态：0-待判题；1-判题中；2-成功；3-失败',
    create_time         datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time         datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_deleted          tinyint  default 0                 null comment '是否删除，逻辑删除,0-未删除，1-已删除'
)
    comment '题目提交表';

