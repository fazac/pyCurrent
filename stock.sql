CREATE DATABASE IF NOT EXISTS stockrealtime DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci;

use stockrealtime;

create table IF NOT EXISTS `em_real_time_stock`
(
    `trade_date`                   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '交易日期',
    `ts_code`                      varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '股票代码',
    `name`                         varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '名称',
    `current_pri`                  decimal(18, 2)                                               DEFAULT NULL COMMENT '最新价',
    `pct_chg`                      decimal(18, 2)                                               DEFAULT NULL COMMENT '涨跌幅',
    `am_chg`                       decimal(18, 2)                                               DEFAULT NULL COMMENT '涨跌额',
    `vol`                          int                                                          DEFAULT NULL COMMENT '成交量（手）',
    `amount`                       decimal(18, 2)                                               DEFAULT NULL COMMENT '成交额（千元）',
    `vibration`                    decimal(18, 2)                                               DEFAULT NULL COMMENT '振幅',
    `pri_high`                     decimal(18, 2)                                               DEFAULT NULL COMMENT '最高价',
    `pri_low`                      decimal(18, 2)                                               DEFAULT NULL COMMENT '最低价',
    `pri_open`                     decimal(18, 2)                                               DEFAULT NULL COMMENT '开盘价',
    `pri_close_pre`                decimal(18, 2)                                               DEFAULT NULL COMMENT '昨收价',
    `vol_ratio`                    decimal(18, 2)                                               DEFAULT NULL COMMENT '量比',
    `change_hand`                  decimal(18, 2)                                               DEFAULT NULL COMMENT '换手率',
    `pe`                           decimal(18, 2)                                               DEFAULT NULL COMMENT '市盈率(动)',
    `pb`                           decimal(18, 2)                                               DEFAULT NULL COMMENT '市净率',
    `market_cap`                   decimal(18, 2)                                               DEFAULT NULL COMMENT '总市值',
    `circulation_market_cap`       decimal(18, 2)                                               DEFAULT NULL COMMENT '流通市值',
    `increase_ratio`               decimal(18, 2)                                               DEFAULT NULL COMMENT '涨速',
    `five_minutes_increase_ratio`  decimal(18, 2)                                               DEFAULT NULL COMMENT '5分钟涨速',
    `sixty_minutes_increase_ratio` decimal(18, 2)                                               DEFAULT NULL COMMENT '60分钟涨速',
    `current_year_ratio`           decimal(18, 2)                                               DEFAULT NULL COMMENT '年初至今涨跌幅',
    KEY `idx_sdl_code` (`ts_code`) USING BTREE,
    KEY `idx_sdl_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

create table IF NOT EXISTS `em_constants`
(
    `c_key`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `c_value`     varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `multi_value` json                                                          DEFAULT NULL COMMENT '多值',
    PRIMARY KEY (`c_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

INSERT INTO `em_constants` (`c_key`, `c_value`, `multi_value`)
VALUES ('CONCERN_CODES', '', NULL);
INSERT INTO `em_constants` (`c_key`, `c_value`, `multi_value`)
VALUES ('HOLD_CODES', '', '[
  {
    \"vol\": 2100,
    \"price\": 16.558,
    \"profit\": -1303.1,
    \"tsCode\": \"300729\",
    \"sellable\": false
  }
]');
INSERT INTO `em_constants` (`c_key`, `c_value`, `multi_value`)
VALUES ('NO_CONCERN_CODES', '', NULL);
INSERT INTO `em_constants` (`c_key`, `c_value`, `multi_value`)
VALUES ('NO_BUY_CODES', '', NULL);
INSERT INTO `em_constants` (`c_key`, `c_value`, `multi_value`)
VALUES ('YESTERDAY_CODES', '', NULL);
INSERT INTO `em_constants` (`c_key`, `c_value`, `multi_value`)
VALUES ('NOTIFICATION', 'TRUE', NULL);

-- 20231229
create table IF NOT EXISTS range_over_code
(
    `trade_date` varchar(8) primary key,
    `code_value` json DEFAULT NULL COMMENT '多值'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- 20240105
create table IF NOT EXISTS real_bar
(
    `trade_date`      varchar(32),
    `ts_code`         varchar(6) comment 'code',
    `cur_pri`         decimal(13, 4),
    `short_sma_price` decimal(13, 4),
    `long_sma_price`  decimal(13, 4),
    `dif`             decimal(13, 4),
    `dea`             decimal(13, 4),
    `bar`             decimal(13, 4),
    KEY `idx_sdl_code` (`ts_code`) USING BTREE,
    KEY `idx_sdl_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

create table IF NOT EXISTS limit_code
(
    `trade_date` varchar(8) primary key,
    `code_value` json DEFAULT NULL COMMENT '多值'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- 20240129
create table IF NOT EXISTS cur_count
(
    trade_date   varchar(32)          not null primary key,
    c_30u        int                  null,
    c_30a        int                  null,
    c_60u        int                  null,
    c_60a        int                  null,
    c_00u        int                  null,
    c_00a        int                  null,
    c_30_5u      int                  null,
    c_30_7d      int                  null,
    c_60_5u      int                  null,
    c_60_7d      int                  null,
    c_00_5u      int                  null,
    c_00_7d      int                  null,
    c_30_35u     int                  null,
    c_60_35u     int                  null,
    c_00_35u     int                  null,
    c_30_13u     int                  null,
    c_60_13u     int                  null,
    c_00_13u     int                  null,
    c_30_01u     int                  null,
    c_60_01u     int                  null,
    c_00_01u     int                  null,
    c_30_01d     int                  null,
    c_60_01d     int                  null,
    c_00_01d     int                  null,
    c_30_13d     int                  null,
    c_60_13d     int                  null,
    c_00_13d     int                  null,
    c_30_37d     int                  null,
    c_60_37d     int                  null,
    c_00_37d     int                  null,
    is_summary   tinyint(1) default 0 null comment '是否最后一条',
    total_amount decimal(30, 2)       null comment '金额',
    zero_amount  decimal(30, 2)       null comment '金额',
    three_amount decimal(30, 2)       null comment '金额',
    six_amount   decimal(30, 2)       null comment '金额',
    KEY `idx_summary` (`is_summary`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- 20240218
create table IF NOT EXISTS `em_d_n_stock`
(
    `ts_code`     varchar(10) CHARACTER SET UTF8MB4 COLLATE UTF8MB4_general_ci NULL DEFAULT NULL COMMENT '股票代码',
    `trade_date`  varchar(8) CHARACTER SET UTF8MB4 COLLATE UTF8MB4_general_ci  NULL DEFAULT NULL COMMENT '交易日期',
    `pri_open`    decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '开盘价',
    `pri_close`   decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '收盘价',
    `pri_high`    decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '最高价',
    `pri_low`     decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '最低价',
    `vol`         int                                                          NULL DEFAULT NULL COMMENT '成交量（手）',
    `amount`      decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '成交额（千元）',
    `amplitude`   decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '振幅',
    `pct_chg`     decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '涨跌幅',
    `am_chg`      decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '涨跌额',
    `change_hand` decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '换手率',
    INDEX `idx_sdl_code` (`ts_code`) USING BTREE,
    INDEX `idx_sdl_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = UTF8MB4
  COLLATE = UTF8MB4_general_ci
  ROW_FORMAT = Dynamic;

create table IF NOT EXISTS `em_d_a_stock`
(
    `ts_code`     varchar(10) CHARACTER SET UTF8MB4 COLLATE UTF8MB4_general_ci NULL DEFAULT NULL COMMENT '股票代码',
    `trade_date`  varchar(8) CHARACTER SET UTF8MB4 COLLATE UTF8MB4_general_ci  NULL DEFAULT NULL COMMENT '交易日期',
    `pri_open`    decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '开盘价',
    `pri_close`   decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '收盘价',
    `pri_high`    decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '最高价',
    `pri_low`     decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '最低价',
    `vol`         int                                                          NULL DEFAULT NULL COMMENT '成交量（手）',
    `amount`      decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '成交额',
    `amplitude`   decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '振幅',
    `pct_chg`     decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '涨跌幅',
    `am_chg`      decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '涨跌额',
    `change_hand` decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '换手率',
    INDEX `idx_sdl_code` (`ts_code`) USING BTREE,
    INDEX `idx_sdl_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = UTF8MB4
  COLLATE = UTF8MB4_general_ci
  ROW_FORMAT = Dynamic;

create table IF NOT EXISTS roc_model
(
    `sn`              int primary key auto_increment,
    `create_time`     datetime,
    `count`           int,
    `s_count`         int comment '特定时间间隔,去除停牌',
    `ratio`           decimal(18, 2),
    `cur_close_pri`   decimal(18, 2),
    `door_pri`        decimal(18, 2),
    `start_date`      varchar(8),
    `end_date`        varchar(8),
    `ts_code`         varchar(6),
    `concept_symbol`  varchar(512),
    `industry_symbol` varchar(32),
    `cap_info`        varchar(32),
    `params`          varchar(256),
    INDEX r_idx (`ts_code`, `create_time`) USING BTREE,
    INDEX r_pa_idx (`ts_code`, `params`) USING BTREE,
    index i_roc_param (`params`) USING BTREE,
    index idx_roc_ct (`create_time`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;


create table IF NOT EXISTS `board_concept_con`
(
    `trade_date`    varchar(32)    null comment '交易日期',
    `symbol`        varchar(32) comment '板块名称',
    `ts_code`       varchar(32) comment '代码',
    `name`          varchar(32) comment '名称',
    `current_pri`   decimal(18, 2) comment '最新价',
    `pct_chg`       decimal(18, 2) null comment '涨跌幅',
    `am_chg`        decimal(18, 2) null comment '涨跌额',
    `vol`           bigint         null comment '成交量（手）',
    `amount`        decimal(18, 2) null comment '成交额',
    `amplitude`     decimal(18, 2) null comment '振幅',
    `pri_high`      decimal(18, 2) null comment '最高价',
    `pri_low`       decimal(18, 2) null comment '最低价',
    `pri_open`      decimal(18, 2) null comment '开盘价',
    `pri_close_pre` decimal(18, 2) null comment '昨日收盘价',
    `change_hand`   decimal(18, 2) null comment '换手率',
    `pe`            decimal(18, 2) null comment '市盈率(动)',
    `pb`            decimal(18, 2) null comment '市净率',
    index board_concept_cons_trade_date (`trade_date`) USING BTREE,
    index board_concept_cons_symbol (`symbol`) USING BTREE,
    index board_concept_cons_name (`name`) USING BTREE,
    index board_concept_cons_code (`ts_code`) USING BTREE,
    INDEX index_code_date (`ts_code`, `trade_date`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;


create table IF NOT EXISTS `board_industry_con`
(
    `trade_date`    varchar(32)    null comment '交易日期',
    `symbol`        varchar(32) comment '板块名称',
    `ts_code`       varchar(32) comment '代码',
    `name`          varchar(32) comment '名称',
    `current_pri`   decimal(18, 2) comment '最新价',
    `pct_chg`       decimal(18, 2) null comment '涨跌幅',
    `am_chg`        decimal(18, 2) null comment '涨跌额',
    `vol`           bigint         null comment '成交量（手）',
    `amount`        decimal(18, 2) null comment '成交额',
    `amplitude`     decimal(18, 2) null comment '振幅',
    `pri_high`      decimal(18, 2) null comment '最高价',
    `pri_low`       decimal(18, 2) null comment '最低价',
    `pri_open`      decimal(18, 2) null comment '开盘价',
    `pri_close_pre` decimal(18, 2) null comment '昨日收盘价',
    `change_hand`   decimal(18, 2) null comment '换手率',
    `pe`            decimal(18, 2) null comment '市盈率(动)',
    `pb`            decimal(18, 2) null comment '市净率',
    index idx_trade_date (trade_date) USING BTREE,
    index idx_symbol (symbol) USING BTREE,
    index idx_name (name) USING BTREE,
    index idx_ts_code (ts_code) USING BTREE,
    INDEX index_code_date (`ts_code`, `trade_date`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;


-- 20240227
-- 20231030
create table IF NOT EXISTS continuous_up
(
    `statics_date` varchar(32) comment '交易日期',
    `ts_code`      varchar(10) comment '股票代码',
    `name`         varchar(8) comment '名称',
    `pri_close`    decimal(18, 3) null comment '收盘价',
    `pri_high`     decimal(18, 3) null comment '最高价',
    `pri_low`      decimal(18, 3) null comment '最低价',
    `up_days`      int comment '连涨天数',
    `up_per`       decimal(18, 2) null comment '连续涨跌幅',
    `change_hand`  decimal(18, 2) comment '累计换手率',
    `industry`     varchar(32) comment '行业',
    index idx_statics_date (statics_date) USING BTREE,
    index idx_statics_code (ts_code) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;


-- 20240502
create table IF NOT EXISTS holiday_date
(
    `date_year`   varchar(4) primary key,
    `date_value`  json DEFAULT NULL COMMENT '原值',
    `after_value` json DEFAULT NULL COMMENT '修改后值'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

create table IF NOT EXISTS board_code
(
    `trade_date` varchar(8) primary key,
    `code_value` varchar(512) COMMENT '多值'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- 20240611
create table IF NOT EXISTS `cur_concern_code`
(
    `trade_date` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `ts_code`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `mark`       varchar(2),
    `rt`         decimal(7, 2),
    `h`          decimal(7, 2),
    `rr`         decimal(7, 2),
    `bp`         decimal(7, 2),
    `cp`         decimal(7, 2),
    `bar`        decimal(7, 2),
    `cm`         decimal(8, 3),
    `pe`         decimal(7, 2),
    `tabel_show` tinyint(1) default '0',
    KEY `idx_ccc_code` (`ts_code`) USING BTREE,
    KEY `idx_ccc_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- 20240627
create table IF NOT EXISTS `code_label`
(
    `trade_date` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `ts_code`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `name`       varchar(16),
    `industry`   varchar(16),
    `concept`    varchar(512),
    KEY `idx_cl_code` (`ts_code`) USING BTREE,
    KEY `idx_cl_date` (`trade_date`) USING BTREE,
    index idx_date_code (`ts_code`, `trade_date`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- 2024-07-08
create table IF NOT EXISTS `last_hand_pri`
(
    `trade_date`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `ts_code`          varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `last_five_pri`    decimal(18, 2),
    `last_ten_pri`     decimal(18, 2),
    `last_twenty_pri`  decimal(18, 2),
    `last_thirty_pri`  decimal(18, 2),
    `last_fifty_pri`   decimal(18, 2),
    `last_hundred_pri` decimal(18, 2),
    `type`             int comment '1 dn , 2 da',
    KEY `idx_lhp_code` (`ts_code`) USING BTREE,
    KEY `idx_lhp_date` (`trade_date`) USING BTREE,
    KEY `idx_lhp_type` (`type`) using BTREE,
    index `idx_code_type` (`ts_code`, `type`) using BTREE,
    index `idx_date_type` (`trade_date`, `type`) using BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- 20241202
create table IF NOT EXISTS em_real_time_etf
(
    trade_date             varchar(32)    null comment '交易日期',
    ts_code                varchar(10)    null comment '股票代码',
    name                   varchar(32)     null comment '名称',
    current_pri            decimal(18, 3) null comment '最新价',
    ipvo                   decimal(18, 3) null comment 'IOPV实时估值',
    discount_ratio         decimal(18, 3) null comment '折价率',
    am_chg                 decimal(18, 3) null comment '涨跌额',
    pct_chg                decimal(18, 3) null comment '涨跌幅',
    vol                    int            null comment '成交量（手）',
    amount                 decimal(18, 3) null comment '成交额（千元）',
    pri_open               decimal(18, 3) null comment '开盘价',
    pri_high               decimal(18, 3) null comment '最高价',
    pri_low                decimal(18, 3) null comment '最低价',
    pri_close_pre          decimal(18, 3) null comment '昨收价',
    vibration              decimal(18, 3) null comment '振幅',
    change_hand            decimal(18, 3) null comment '换手率',
    vol_ratio              decimal(18, 3) null comment '量比',
    commission_ratio       decimal(18, 3) null comment '委比',
    outer_disc             decimal(18, 3) null comment '外盘',
    inner_disc             decimal(18, 3) null comment '内盘',

    main_fund              decimal(18, 3) null comment '主力净流入-净额',
    main_fund_per          decimal(18, 3) null comment '主力净流入-净占比',
    larger_order           decimal(18, 3) null comment '超大单-净额',
    larger_order_per       decimal(18, 3) null comment '超大单-净占比',
    large_order            decimal(18, 3) null comment '大单-净额',
    large_order_per        decimal(18, 3) null comment '大单-净占比',
    medium_order           decimal(18, 3) null comment '中单-净额',
    medium_order_per       decimal(18, 3) null comment '中单-净占比',
    small_order            decimal(18, 3) null comment '小单-净额',
    small_order_per        decimal(18, 3) null comment '小单-净占比',
    cur_hand               decimal(18, 3) null comment '现手',
    buy_first              decimal(18, 3) null comment '买一',
    sell_first             decimal(18, 3) null comment '卖一',
    latest_share           decimal(18, 3) null comment '最新份额',
    circulation_market_cap decimal(18, 3) null comment '流通市值',
    market_cap             decimal(18, 3) null comment '总市值',
    data_date              varchar(32)    null comment '数据日期',
    cur_date               varchar(32)    null comment '更新时间',
    KEY `idx_etf_code` (`ts_code`) USING BTREE,
    KEY `idx_etf_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

create table IF NOT EXISTS `em_d_a_etf`
(
    `ts_code`     varchar(10) CHARACTER SET UTF8MB4 COLLATE UTF8MB4_general_ci NULL DEFAULT NULL COMMENT '股票代码',
    `trade_date`  varchar(8) CHARACTER SET UTF8MB4 COLLATE UTF8MB4_general_ci  NULL DEFAULT NULL COMMENT '交易日期',
    `pri_open`    decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '开盘价',
    `pri_close`   decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '收盘价',
    `pri_high`    decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '最高价',
    `pri_low`     decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '最低价',
    `vol`         int                                                          NULL DEFAULT NULL COMMENT '成交量（手）',
    `amount`      decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '成交额',
    `amplitude`   decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '振幅',
    `pct_chg`     decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '涨跌幅',
    `am_chg`      decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '涨跌额',
    `change_hand` decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '换手率',
    INDEX `idx_sdl_code` (`ts_code`) USING BTREE,
    INDEX `idx_sdl_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = UTF8MB4
  COLLATE = UTF8MB4_general_ci
  ROW_FORMAT = Dynamic;

create table IF NOT EXISTS `em_d_n_etf`
(
    `ts_code`     varchar(10) CHARACTER SET UTF8MB4 COLLATE UTF8MB4_general_ci NULL DEFAULT NULL COMMENT '股票代码',
    `trade_date`  varchar(8) CHARACTER SET UTF8MB4 COLLATE UTF8MB4_general_ci  NULL DEFAULT NULL COMMENT '交易日期',
    `pri_open`    decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '开盘价',
    `pri_close`   decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '收盘价',
    `pri_high`    decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '最高价',
    `pri_low`     decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '最低价',
    `vol`         int                                                          NULL DEFAULT NULL COMMENT '成交量（手）',
    `amount`      decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '成交额',
    `amplitude`   decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '振幅',
    `pct_chg`     decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '涨跌幅',
    `am_chg`      decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '涨跌额',
    `change_hand` decimal(18, 2)                                               NULL DEFAULT NULL COMMENT '换手率',
    INDEX `idx_sdl_code` (`ts_code`) USING BTREE,
    INDEX `idx_sdl_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = UTF8MB4
  COLLATE = UTF8MB4_general_ci
  ROW_FORMAT = Dynamic;

create table IF NOT EXISTS yf_usr
(
    `sn`       int auto_increment comment 'sn',
    `name`     varchar(10) comment '名称',
    `phone`    varchar(11) comment '手机号',
    `email`    varchar(32) comment 'emial',
    `img`      varchar(128) comment '图像',
    `uid`      varchar(64) COMMENT '唯一id',
    `password` varchar(24) comment '密码,md5加密',
    `salt`     varchar(13),
    `totp_sk`  varchar(32),
    `totp_img` varchar(256) comment '二维码地址',
    PRIMARY key (`sn`)
) ENGINE = InnoDB
  CHARACTER SET = UTF8MB4
  COLLATE = UTF8MB4_general_ci
  ROW_FORMAT = Dynamic;

create table IF NOT EXISTS yf_token
(
    `sn`            int auto_increment comment 'sn',
    `user_sn`       int comment '用户sn',
    `token`         varchar(1024) comment 'token',
    `uid`           varchar(64) COMMENT '保证设备唯一性的id, 微信openid或android设备的唯一ID',
    `device`        varchar(32) COMMENT '申请token的项目类型, DEVICE_WX等',
    `expire_date`   datetime COMMENT 'token失效时间',
    `update_time`   datetime COMMENT '更新时间',
    `ip`            varchar(40) COMMENT 'ip地址',
    `token_salt`    varchar(32),
    `unionid`       varchar(64) COMMENT '微信的unionid,全局唯一id',
    `third_session` varchar(64) COMMENT '三方平台的登陆session, 微信小程序中为session_key',
    `public_key`    varchar(512) comment '公钥',
    PRIMARY key (`sn`)
) ENGINE = InnoDB
  CHARACTER SET = UTF8MB4
  COLLATE = UTF8MB4_general_ci
  ROW_FORMAT = Dynamic;

create table if not exists `stock_cal_model`
(
    ts_code    varchar(6),
    trade_date varchar(8),
    price      decimal(18, 3),
    level      int,
    type       int comment '1 da, 2 lhp 10 ,3 30 ,4 50 ,5 100',
    KEY `idx_sdl_code` (`ts_code`) USING BTREE,
    KEY `idx_sdl_date` (`trade_date`) USING BTREE,
    INDEX r_idx (`ts_code`, `type`, `trade_date`) USING BTREE,
    INDEX r_idx_level (`ts_code`, `type`, `level`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

