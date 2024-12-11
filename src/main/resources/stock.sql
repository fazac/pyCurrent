drop database stockrealtime;
CREATE
    DATABASE IF NOT EXISTS stockrealtime
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci;

use
    stockrealtime;

drop table if exists `em_real_time_stock`;
CREATE TABLE `em_real_time_stock`
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

drop table if exists `em_constants`;
CREATE TABLE `em_constants`
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
drop table if exists range_over_code;
create table range_over_code
(
    `trade_date` varchar(8) primary key,
    `code_value` json DEFAULT NULL COMMENT '多值'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- 20240105
drop table if exists real_bar;
create table real_bar
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

drop table if exists limit_code;
create table limit_code
(
    `trade_date` varchar(8) primary key,
    `code_value` json DEFAULT NULL COMMENT '多值'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- 20240129
drop table if exists cur_count;
create table cur_count
(
    `trade_date` varchar(32) primary key,
    `c_30u`      int,
    `c_30a`      int,
    `c_60u`      int,
    `c_60a`      int,
    `c_00u`      int,
    `c_00a`      int
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- 20240218
drop table if exists `em_d_n_stock`;
CREATE TABLE `em_d_n_stock`
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

drop table if exists `em_d_a_stock`;
CREATE TABLE `em_d_a_stock`
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

drop table if exists `roc_model`;
create table roc_model
(
    `sn`              int primary key auto_increment,
    `create_time`     datetime,
    `count`           int,
    `ratio`           decimal(18, 2),
    `cur_close_pri`   decimal(18, 2),
    `door_pri`        decimal(18, 2),
    `start_date`      varchar(8),
    `end_date`        varchar(8),
    `ts_code`         varchar(6),
    `concept_symbol`  varchar(512),
    `industry_symbol` varchar(32),
    `cap_info`        varchar(32),
    `params`          varchar(256)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

alter table roc_model
    add index r_idx (`ts_code`, `create_time`);
alter table roc_model
    add index r_pa_idx (`ts_code`, `params`);
alter table `roc_model`
    add index i_roc_param (`params`);
alter table roc_model
    add index idx_roc_ct (`create_time`);

drop table if exists `board_concept_con`;
create table `board_concept_con`
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
    `pb`            decimal(18, 2) null comment '市净率'
) charset = utf8mb4;
create index board_concept_cons_trade_date
    on board_concept_con (trade_date);
create index board_concept_cons_symbol
    on board_concept_con (symbol);
create index board_concept_cons_name
    on board_concept_con (name);
create index board_concept_cons_code
    on board_concept_con (ts_code);

drop table if exists `board_industry_con`;
create table `board_industry_con`
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
    `pb`            decimal(18, 2) null comment '市净率'
) charset = utf8mb4;
create index idx_trade_date
    on board_industry_con (trade_date);
create index idx_symbol
    on board_industry_con (symbol);
create index idx_name
    on board_industry_con (name);
create index idx_ts_code
    on board_industry_con (ts_code);


-- 20240227
-- 20231030
create table continuous_up
(
    `statics_date` varchar(32) comment '交易日期',
    `sort`         int comment '序号',
    `ts_code`      varchar(10) comment '股票代码',
    `name`         varchar(8) comment '名称',
    `pri_close`    decimal(18, 3) null comment '收盘价',
    `pri_high`     decimal(18, 3) null comment '最高价',
    `pri_low`      decimal(18, 3) null comment '最低价',
    `up_days`      int comment '连涨天数',
    `up_per`       decimal(18, 2) null comment '连续涨跌幅',
    `change_hand`  decimal(18, 2) comment '累计换手率',
    `industry`     varchar(32) comment '行业'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

create index idx_statics_date on `continuous_up` (statics_date);
create index idx_statics_code on `continuous_up` (ts_code);
alter table continuous_up
    drop
        column `sort`;

ALTER DATABASE stockrealtime CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

ALTER TABLE `board_concept_con`
    CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
ALTER TABLE `board_industry_con`
    CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 20240328
alter table `cur_count`
    add column `c_30_5u` int;
alter table `cur_count`
    add column `c_30_7d` int;
alter table `cur_count`
    add column `c_60_5u` int;
alter table `cur_count`
    add column `c_60_7d` int;
alter table `cur_count`
    add column `c_00_5u` int;
alter table `cur_count`
    add column `c_00_7d` int;

-- 20240502
drop table if exists holiday_date;
create table holiday_date
(
    `date_year`   varchar(4) primary key,
    `date_value`  json DEFAULT NULL COMMENT '原值',
    `after_value` json DEFAULT NULL COMMENT '修改后值'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- 20240529

alter table `cur_count`
    add column `c_30_35u` int;
alter table `cur_count`
    add column `c_60_35u` int;
alter table `cur_count`
    add column `c_00_35u` int;

alter table `cur_count`
    add column `c_30_13u` int;
alter table `cur_count`
    add column `c_60_13u` int;
alter table `cur_count`
    add column `c_00_13u` int;

alter table `cur_count`
    add column `c_30_01u` int;
alter table `cur_count`
    add column `c_60_01u` int;
alter table `cur_count`
    add column `c_00_01u` int;

alter table `cur_count`
    add column `c_30_01d` int;
alter table `cur_count`
    add column `c_60_01d` int;
alter table `cur_count`
    add column `c_00_01d` int;

alter table `cur_count`
    add column `c_30_13d` int;
alter table `cur_count`
    add column `c_60_13d` int;
alter table `cur_count`
    add column `c_00_13d` int;

alter table `cur_count`
    add column `c_30_37d` int;
alter table `cur_count`
    add column `c_60_37d` int;
alter table `cur_count`
    add column `c_00_37d` int;

drop table if exists board_code;
create table board_code
(
    `trade_date` varchar(8) primary key,
    `code_value` varchar(512) COMMENT '多值'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- 20240611
create table `cur_concern_code`
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
    KEY `idx_ccc_code` (`ts_code`) USING BTREE,
    KEY `idx_ccc_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- 20240625
alter table cur_concern_code
    add column `tabel_show` tinyint(1) default '0';

-- 20240626
ALTER TABLE `board_concept_con`
    ADD INDEX index_code_date (`ts_code`, `trade_date`);
ALTER TABLE `board_industry_con`
    ADD INDEX index_code_date (`ts_code`, `trade_date`);

-- 20240627
create table `code_label`
(
    `trade_date` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `ts_code`    varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `name`       varchar(16),
    `industry`   varchar(16),
    `concept`    varchar(512),
    KEY `idx_cl_code` (`ts_code`) USING BTREE,
    KEY `idx_cl_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

alter table `code_label`
    add index idx_date_code (`ts_code`, `trade_date`);

alter table `code_label`
    drop column `name`;

-- 20240704
alter table `code_label`
    add column `name` varchar(16);

alter table `cur_count`
    add column `is_summary` tinyint(1) default '0' comment '是否最后一条';
alter table `cur_count`
    add column `total_amount` decimal(30, 2) comment '金额';
alter table `cur_count`
    add column `zero_amount` decimal(30, 2) comment '金额';
alter table `cur_count`
    add column `three_amount` decimal(30, 2) comment '金额';
alter table `cur_count`
    add column `six_amount` decimal(30, 2) comment '金额';

alter table `cur_count`
    add index `idx_summary` (is_summary);

-- 20240705

select concat(left(trade_date, 4), '-', substr(trade_date, 5, 2), '-', substr(trade_date, 7, 2),
              ' 15:01:00')                                                              as trade_date,
       count(case when ts_code like '3%' and pct_chg >= 0 then 1 end)                   as c_30u,
       count(case when ts_code like '3%' then 1 end)                                    as c_30a,
       count(case when ts_code like '6%' and pct_chg >= 0 then 1 end)                   as c_60u,
       count(case when ts_code like '6%' then 1 end)                                    as c_60a,
       count(case when ts_code like '0%' and pct_chg >= 0 then 1 end)                   as c_00u,
       count(case when ts_code like '0%' then 1 end)                                    as c_00a,
       count(case when ts_code like '3%' and pct_chg >= 5 then 1 end)                   as c_30_5u,
       count(case when ts_code like '3%' and pct_chg < -7 then 1 end)                   as c_30_7d,
       count(case when ts_code like '6%' and pct_chg >= 5 then 1 end)                   as c_60_5u,
       count(case when ts_code like '6%' and pct_chg < -7 then 1 end)                   as c_60_7d,
       count(case when ts_code like '0%' and pct_chg >= 5 then 1 end)                   as c_00_5u,
       count(case when ts_code like '0%' and pct_chg < -7 then 1 end)                   as c_00_7d,
       count(case when ts_code like '3%' and pct_chg >= 3 and pct_chg < 5 then 1 end)   as c_30_35u,
       count(case when ts_code like '6%' and pct_chg >= 3 and pct_chg < 5 then 1 end)   as c_60_35u,
       count(case when ts_code like '0%' and pct_chg >= 3 and pct_chg < 5 then 1 end)   as c_00_35u,
       count(case when ts_code like '3%' and pct_chg >= 1 and pct_chg < 3 then 1 end)   as c_30_13u,
       count(case when ts_code like '6%' and pct_chg >= 1 and pct_chg < 3 then 1 end)   as c_60_13u,
       count(case when ts_code like '0%' and pct_chg >= 1 and pct_chg < 3 then 1 end)   as c_00_13u,
       count(case when ts_code like '3%' and pct_chg >= 0 and pct_chg < 1 then 1 end)   as c_30_01u,
       count(case when ts_code like '6%' and pct_chg >= 0 and pct_chg < 1 then 1 end)   as c_60_01u,
       count(case when ts_code like '0%' and pct_chg >= 0 and pct_chg < 1 then 1 end)   as c_00_01u,
       count(case when ts_code like '3%' and pct_chg >= -1 and pct_chg < 0 then 1 end)  as c_30_01d,
       count(case when ts_code like '6%' and pct_chg >= -1 and pct_chg < 0 then 1 end)  as c_60_01d,
       count(case when ts_code like '0%' and pct_chg >= -1 and pct_chg < 0 then 1 end)  as c_00_01d,
       count(case when ts_code like '3%' and pct_chg >= -3 and pct_chg < -1 then 1 end) as c_30_13d,
       count(case when ts_code like '6%' and pct_chg >= -3 and pct_chg < -1 then 1 end) as c_60_13d,
       count(case when ts_code like '0%' and pct_chg >= -3 and pct_chg < -1 then 1 end) as c_00_13d,
       count(case when ts_code like '3%' and pct_chg >= -7 and pct_chg < -3 then 1 end) as c_30_37d,
       count(case when ts_code like '6%' and pct_chg >= -7 and pct_chg < -3 then 1 end) as c_60_37d,
       count(case when ts_code like '0%' and pct_chg >= -7 and pct_chg < -3 then 1 end) as c_00_37d,
       '1'                                                                              as is_summary,
       sum(amount)                                                                      as total_amount,
       sum(case when ts_code like '0%' then amount end)                                 as zero_amount,
       sum(case when ts_code like '3%' then amount end)                                 as three_amount,
       sum(case when ts_code like '6%' then amount end)                                 as six_amount
from em_d_n_stock
where trade_date > '20240101'
group by trade_date;


-- 2024-07-08
drop table if exists `last_hand_pri`;
create table `last_hand_pri`
(
    `trade_date`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `ts_code`          varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
    `current_pri`      decimal(18, 2),
    `last_five_pri`    decimal(18, 2),
    `last_ten_pri`     decimal(18, 2),
    `last_twenty_pri`  decimal(18, 2),
    `last_thirty_pri`  decimal(18, 2),
    `last_fifty_pri`   decimal(18, 2),
    `last_hundred_pri` decimal(18, 2),
    KEY `idx_lhp_code` (`ts_code`) USING BTREE,
    KEY `idx_lhp_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- 20240709
alter table last_hand_pri
    drop column `current_pri`;

-- 20241202
drop table if exists `em_real_time_etf`;
create table em_real_time_etf
(
    trade_date             varchar(32)    null comment '交易日期',
    ts_code                varchar(10)    null comment '股票代码',
    name                   varchar(8)     null comment '名称',
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
    data_date              varchar(16)    null comment '数据日期',
    cur_date               varchar(32)    null comment '更新时间',
    KEY `idx_etf_code` (`ts_code`) USING BTREE,
    KEY `idx_etf_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

drop table if exists `em_d_a_etf`;
CREATE TABLE `em_d_a_etf`
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

drop table if exists `em_d_n_etf`;
CREATE TABLE `em_d_n_etf`
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

-- 20241211
drop table if exists `stock_cal_model`;
create table `stock_cal_model`
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
