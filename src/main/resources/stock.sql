CREATE TABLE `stock`
(
    `ts_code`       varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
    `trade_date`    varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '交易日期',
    `pri_open`      decimal(18, 2)                                         NULL DEFAULT NULL COMMENT '开盘价',
    `pri_high`      decimal(18, 2)                                         NULL DEFAULT NULL COMMENT '最高价',
    `pri_low`       decimal(18, 2)                                         NULL DEFAULT NULL COMMENT '最低价',
    `pri_close`     decimal(18, 2)                                         NULL DEFAULT NULL COMMENT '收盘价',
    `pri_close_pre` decimal(18, 2)                                         NULL DEFAULT NULL COMMENT '昨收价',
    `am_chg`        decimal(18, 2)                                         NULL DEFAULT NULL COMMENT '涨跌额',
    `pct_chg`       decimal(18, 2)                                         NULL DEFAULT NULL COMMENT '涨跌幅（未复权）',
    `vol`           int                                                    NULL DEFAULT NULL COMMENT '成交量（手）',
    `amount`        decimal(18, 2)                                         NULL DEFAULT NULL COMMENT '成交额（千元）',
    INDEX `idx_sdl_code` (`ts_code`) USING BTREE,
    INDEX `idx_sdl_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- 20230216
create table stock_kdj
(
    `ts_code`    varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '股票代码',
    `trade_date` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '交易日期',
    `rsv`        decimal(9, 5) comment 'rsv, :(收盘价-N日内最低价的最低值)/(N日内最高价的最高值-N日内最低价的最低值)*100',
    `k_value`    decimal(9, 3) comment 'k值',
    `d_value`    decimal(9, 3) comment 'd值',
    `j_value`    decimal(9, 3) comment 'j值',
    INDEX `idx_sdl_code` (`ts_code`) USING BTREE,
    INDEX `idx_sdl_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

alter table stock_kdj
    modify column `k_value` decimal(6, 2) comment 'k值',
    modify column `d_value` decimal(6, 2) comment 'd值',
    modify column `j_value` decimal(6, 2) comment 'j值';

-- 20230219
alter table stock_kdj
    modify column `k_value` decimal(9, 3) comment 'k值',
    modify column `d_value` decimal(9, 3) comment 'd值',
    modify column `j_value` decimal(9, 3) comment 'j值';

-- 20230220

-- 实时行情
create table em_real_time_stock
(
    `trade_date`                   varchar(32) comment '交易日期',
    `ts_code`                      varchar(10) COMMENT '股票代码',
    `name`                         varchar(8) COMMENT '名称',
    `current_pri`                  decimal(18, 2) comment '最新价',
    `pct_chg`                      decimal(18, 2) COMMENT '涨跌幅',
    `am_chg`                       decimal(18, 2) COMMENT '涨跌额',
    `vol`                          int COMMENT '成交量（手）',
    `amount`                       decimal(18, 2) COMMENT '成交额（千元）',
    `vibration`                    decimal(18, 2) comment '振幅',
    `pri_high`                     decimal(18, 2) COMMENT '最高价',
    `pri_low`                      decimal(18, 2) COMMENT '最低价',
    `pri_open`                     decimal(18, 2) COMMENT '开盘价',
    `pri_close_pre`                decimal(18, 2) COMMENT '昨收价',
    `vol_ratio`                    decimal(18, 2) comment '量比',
    `change_hand`                  decimal(18, 2) comment '换手率',
    `pe`                           decimal(18, 2) comment '市盈率(动)',
    `pb`                           decimal(18, 2) comment '市净率',
    `market_cap`                   decimal(18, 2) comment '总市值',
    `circulation_market_cap`       decimal(18, 2) comment '流通市值',
    `increase_ratio`               decimal(18, 2) comment '涨速',
    `five_minutes_increase_ratio`  decimal(18, 2) comment '5分钟涨速',
    `sixty_minutes_increase_ratio` decimal(18, 2) comment '60分钟涨速',
    `current_year_ratio`           decimal(18, 2) comment '年初至今涨跌幅',
    INDEX `idx_sdl_code` (`ts_code`) USING BTREE,
    INDEX `idx_sdl_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;


-- 实时新股行情
create table em_real_time_new_stock
(
    `trade_date`                   varchar(32) comment '交易日期',
    `ts_code`                      varchar(10) COMMENT '股票代码',
    `name`                         varchar(8) COMMENT '名称',
    `current_pri`                  decimal(18, 2) comment '最新价',
    `pct_chg`                      decimal(18, 2) NULL DEFAULT NULL COMMENT '涨跌幅',
    `am_chg`                       decimal(18, 2) NULL DEFAULT NULL COMMENT '涨跌额',
    `vol`                          int            NULL DEFAULT NULL COMMENT '成交量（手）',
    `amount`                       decimal(18, 2) NULL DEFAULT NULL COMMENT '成交额（千元）',
    `vibration`                    decimal(18, 2) comment '振幅',
    `pri_high`                     decimal(18, 2) NULL DEFAULT NULL COMMENT '最高价',
    `pri_low`                      decimal(18, 2) NULL DEFAULT NULL COMMENT '最低价',
    `pri_open`                     decimal(18, 2) NULL DEFAULT NULL COMMENT '开盘价',
    `pri_close_pre`                decimal(18, 2) NULL DEFAULT NULL COMMENT '昨收价',
    `vol_ratio`                    decimal(18, 2) comment '量比',
    `change_hand`                  decimal(18, 2) comment '换手率',
    `pe`                           decimal(18, 2) comment '市盈率(动)',
    `pb`                           decimal(18, 2) comment '市净率',
    `on_market`                    varchar(32) comment '上市时间',
    `market_cap`                   decimal(18, 2) comment '总市值',
    `circulation_market_cap`       decimal(18, 2) comment '流通市值',
    `increase_ratio`               decimal(18, 2) comment '涨速',
    `five_minutes_increase_ratio`  decimal(18, 2) comment '5分钟涨速',
    `sixty_minutes_increase_ratio` decimal(18, 2) comment '60分钟涨速',
    `current_year_ratio`           decimal(18, 2) comment '年初至今涨跌幅',
    INDEX `idx_sdl_code` (`ts_code`) USING BTREE,
    INDEX `idx_sdl_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- 实时创业板行情
create table em_real_time_chi_next
(
    `trade_date`                   varchar(32) comment '交易日期',
    `ts_code`                      varchar(10) COMMENT '股票代码',
    `name`                         varchar(8) COMMENT '名称',
    `current_pri`                  decimal(18, 2) comment '最新价',
    `pct_chg`                      decimal(18, 2) COMMENT '涨跌幅',
    `am_chg`                       decimal(18, 2) COMMENT '涨跌额',
    `vol`                          int COMMENT '成交量（手）',
    `amount`                       decimal(18, 2) COMMENT '成交额（千元）',
    `vibration`                    decimal(18, 2) comment '振幅',
    `pri_high`                     decimal(18, 2) COMMENT '最高价',
    `pri_low`                      decimal(18, 2) COMMENT '最低价',
    `pri_open`                     decimal(18, 2) COMMENT '开盘价',
    `pri_close_pre`                decimal(18, 2) COMMENT '昨收价',
    `vol_ratio`                    decimal(18, 2) comment '量比',
    `change_hand`                  decimal(18, 2) comment '换手率',
    `pe`                           decimal(18, 2) comment '市盈率(动)',
    `pb`                           decimal(18, 2) comment '市净率',
    `market_cap`                   decimal(18, 2) comment '总市值',
    `circulation_market_cap`       decimal(18, 2) comment '流通市值',
    `increase_ratio`               decimal(18, 2) comment '涨速',
    `five_minutes_increase_ratio`  decimal(18, 2) comment '5分钟涨速',
    `sixty_minutes_increase_ratio` decimal(18, 2) comment '60分钟涨速',
    `current_year_ratio`           decimal(18, 2) comment '年初至今涨跌幅',
    INDEX `idx_sdl_code` (`ts_code`) USING BTREE,
    INDEX `idx_sdl_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;


-- 20230223
CREATE TABLE `em_basic_stock`
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

CREATE TABLE `em_daily_before_rehabilitation_stock`
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

-- 20230224

CREATE TABLE `em_daily_after_rehabilitation_stock`
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

CREATE TABLE `em_weekly_before_rehabilitation_stock`
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


CREATE TABLE `em_weekly_after_rehabilitation_stock`
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

CREATE TABLE `em_monthly_before_rehabilitation_stock`
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


CREATE TABLE `em_monthly_after_rehabilitation_stock`
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


CREATE TABLE `em_weekly_no_rehabilitation_stock`
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


CREATE TABLE `em_monthly_no_rehabilitation_stock`
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

-- 20230226
create table em_da_kdj
(
    `ts_code`    varchar(10) CHARACTER SET UTF8MB4 COLLATE UTF8MB4_general_ci NULL DEFAULT NULL COMMENT '股票代码',
    `trade_date` varchar(8) CHARACTER SET UTF8MB4 COLLATE UTF8MB4_general_ci  NULL DEFAULT NULL COMMENT '交易日期',
    `rsv`        decimal(9, 5) comment 'rsv, :(收盘价-N日内最低价的最低值)/(N日内最高价的最高值-N日内最低价的最低值)*100',
    `k_value`    decimal(9, 3) comment 'k值',
    `d_value`    decimal(9, 3) comment 'd值',
    `j_value`    decimal(9, 3) comment 'j值',
    INDEX `idx_sdl_code` (`ts_code`) USING BTREE,
    INDEX `idx_sdl_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = UTF8MB4
  COLLATE = UTF8MB4_general_ci
  ROW_FORMAT = Dynamic;

-- 20230301
alter table `em_basic_stock` rename `em_d_n_stock`;
alter table `em_daily_before_rehabilitation_stock` rename `em_d_b_stock`;
alter table `em_daily_after_rehabilitation_stock` rename `em_d_a_stock`;
alter table `em_weekly_no_rehabilitation_stock` rename `em_w_n_stock`;
alter table `em_weekly_before_rehabilitation_stock` rename `em_w_b_stock`;
alter table `em_weekly_after_rehabilitation_stock` rename `em_w_a_stock`;
alter table `em_monthly_no_rehabilitation_stock` rename `em_m_n_stock`;
alter table `em_monthly_before_rehabilitation_stock` rename `em_m_b_stock`;
alter table `em_monthly_after_rehabilitation_stock` rename `em_m_a_stock`;
alter table `em_da_kdj` rename `em_d_a_kdj`;
alter table `stock` rename `tu_stock`;
alter table `stock_kdj` rename `tu_kdj`;


-- 20230512
alter table em_d_a_kdj
    modify column rsv decimal(18, 5) null comment 'rsv, :(收盘价-N日内最低价的最低值)/(N日内最高价的最高值-N日内最低价的最低值)*100';

-- 20230515
create table `board_concept`
(
    `trade_date`   varchar(32)    null comment '交易日期',
    `sort_id`      int comment '排名',
    `name`         varchar(32) comment '名称',
    `ts_code`      varchar(32) comment '代码',
    `current_pri`  decimal(18, 2) comment '最新价',
    `am_chg`       decimal(18, 2) null comment '涨跌额',
    `pct_chg`      decimal(18, 2) null comment '涨跌幅',
    `market_cap`   decimal(18, 2) null comment '总市值',
    `change_hand`  decimal(18, 2) null comment '换手率',
    `up_count`     int comment '上涨家数',
    `down_count`   int comment '下跌家数',
    `lead_name`    varchar(32) comment '领涨股票',
    `lead_pct_chg` decimal(18, 2) null comment '领涨股票涨跌幅'
) charset = utf8mb4;
create index board_concept_trade_date
    on board_concept (trade_date);
create index board_concept_ts_code
    on board_concept (ts_code);

create table `board_concept_d_a`
(
    name        varchar(32)    null comment '概念名称',
    trade_date  varchar(8)     null comment '交易日期',
    pri_open    decimal(18, 2) null comment '开盘价',
    pri_close   decimal(18, 2) null comment '收盘价',
    pri_high    decimal(18, 2) null comment '最高价',
    pri_low     decimal(18, 2) null comment '最低价',
    pct_chg     decimal(18, 2) null comment '涨跌幅',
    am_chg      decimal(18, 2) null comment '涨跌额',
    vol         int            null comment '成交量（手）',
    amount      decimal(18, 2) null comment '成交额',
    amplitude   decimal(18, 2) null comment '振幅',
    change_hand decimal(18, 2) null comment '换手率'
) collate = utf8mb4_general_ci;
create index idx_sdl_code
    on board_concept_d_a (name);
create index idx_sdl_date
    on board_concept_d_a (trade_date);

create table `board_concept_d_n`
(
    name        varchar(32)    null comment '概念名称',
    trade_date  varchar(8)     null comment '交易日期',
    pri_open    decimal(18, 2) null comment '开盘价',
    pri_close   decimal(18, 2) null comment '收盘价',
    pri_high    decimal(18, 2) null comment '最高价',
    pri_low     decimal(18, 2) null comment '最低价',
    pct_chg     decimal(18, 2) null comment '涨跌幅',
    am_chg      decimal(18, 2) null comment '涨跌额',
    vol         int            null comment '成交量（手）',
    amount      decimal(18, 2) null comment '成交额',
    amplitude   decimal(18, 2) null comment '振幅',
    change_hand decimal(18, 2) null comment '换手率'
) collate = utf8mb4_general_ci;
create index idx_sdl_code
    on board_concept_d_n (name);
create index idx_sdl_date
    on board_concept_d_n (trade_date);


create table `board_concept_w_a`
(
    name        varchar(32)    null comment '概念名称',
    trade_date  varchar(8)     null comment '交易日期',
    pri_open    decimal(18, 2) null comment '开盘价',
    pri_close   decimal(18, 2) null comment '收盘价',
    pri_high    decimal(18, 2) null comment '最高价',
    pri_low     decimal(18, 2) null comment '最低价',
    pct_chg     decimal(18, 2) null comment '涨跌幅',
    am_chg      decimal(18, 2) null comment '涨跌额',
    vol         int            null comment '成交量（手）',
    amount      decimal(18, 2) null comment '成交额',
    amplitude   decimal(18, 2) null comment '振幅',
    change_hand decimal(18, 2) null comment '换手率'
) collate = utf8mb4_general_ci;
create index idx_sdl_code
    on board_concept_w_a (name);
create index idx_sdl_date
    on board_concept_w_a (trade_date);

create table `board_concept_w_n`
(
    name        varchar(32)    null comment '概念名称',
    trade_date  varchar(8)     null comment '交易日期',
    pri_open    decimal(18, 2) null comment '开盘价',
    pri_close   decimal(18, 2) null comment '收盘价',
    pri_high    decimal(18, 2) null comment '最高价',
    pri_low     decimal(18, 2) null comment '最低价',
    pct_chg     decimal(18, 2) null comment '涨跌幅',
    am_chg      decimal(18, 2) null comment '涨跌额',
    vol         int            null comment '成交量（手）',
    amount      decimal(18, 2) null comment '成交额',
    amplitude   decimal(18, 2) null comment '振幅',
    change_hand decimal(18, 2) null comment '换手率'
) collate = utf8mb4_general_ci;
create index idx_sdl_code
    on board_concept_w_n (name);
create index idx_sdl_date
    on board_concept_w_n (trade_date);

-- 20230516
alter table board_concept_w_n
    modify column `vol` bigint null comment '成交量（手）';

alter table board_concept_w_a
    modify column `vol` bigint null comment '成交量（手）';

alter table board_concept_d_n
    modify column `vol` bigint null comment '成交量（手）';

alter table board_concept_d_a
    modify column `vol` bigint null comment '成交量（手）';

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

create table em_real_time_etf
(
    trade_date             varchar(32)    null comment '交易日期',
    ts_code                varchar(10)    null comment '股票代码',
    name                   varchar(32)    null comment '名称',
    current_pri            decimal(18, 3) null comment '最新价',
    am_chg                 decimal(18, 3) null comment '涨跌额',
    pct_chg                decimal(18, 3) null comment '涨跌幅',
    vol                    bigint         null comment '成交量（手）',
    amount                 decimal(18, 3) null comment '成交额（千元）',
    pri_open               decimal(18, 3) null comment '开盘价',
    pri_high               decimal(18, 3) null comment '最高价',
    pri_low                decimal(18, 3) null comment '最低价',
    pri_close_pre          decimal(18, 3) null comment '昨收价',
    change_hand            decimal(18, 3) null comment '换手率',
    circulation_market_cap decimal(18, 3) null comment '流通市值',
    market_cap             decimal(18, 3) null comment '总市值'
) charset = utf8mb4;
create index inx_trade_date
    on em_real_time_etf (trade_date);
create index inx_ts_code
    on em_real_time_etf (ts_code);

create table em_etf
(
    ts_code     varchar(10)    null comment '股票代码',
    trade_date  varchar(8)     null comment '交易日期',
    pri_open    decimal(18, 3) null comment '开盘价',
    pri_close   decimal(18, 3) null comment '收盘价',
    pri_high    decimal(18, 3) null comment '最高价',
    pri_low     decimal(18, 3) null comment '最低价',
    vol         bigint         null comment '成交量（手）',
    amount      decimal(18, 3) null comment '成交额',
    amplitude   decimal(18, 3) null comment '振幅',
    pct_chg     decimal(18, 3) null comment '涨跌幅',
    am_chg      decimal(18, 3) null comment '涨跌额',
    change_hand decimal(18, 3) null comment '换手率',
    unit        varchar(10) comment 'period: daily,weekly,monthly',
    adjust      varchar(3) comment '复权方式: qfq, hfq, 空'
) collate = utf8mb4_general_ci;

create index idx_code
    on em_etf (ts_code);
create index idx_date
    on em_etf (trade_date);
create index idx_unit
    on em_etf (unit);
create index idx_adjust
    on em_etf (adjust);

-- 20230517
create table `board_industry`
(
    `trade_date`   varchar(32)    null comment '交易日期',
    `sort_id`      int comment '排名',
    `name`         varchar(32) comment '名称',
    `ts_code`      varchar(32) comment '代码',
    `current_pri`  decimal(18, 2) comment '最新价',
    `am_chg`       decimal(18, 2) null comment '涨跌额',
    `pct_chg`      decimal(18, 2) null comment '涨跌幅',
    `market_cap`   decimal(18, 2) null comment '总市值',
    `change_hand`  decimal(18, 2) null comment '换手率',
    `up_count`     int comment '上涨家数',
    `down_count`   int comment '下跌家数',
    `lead_name`    varchar(32) comment '领涨股票',
    `lead_pct_chg` decimal(18, 2) null comment '领涨股票涨跌幅'
) charset = utf8mb4;
create index idx_trade_date
    on board_industry (trade_date);
create index idx_name
    on board_industry (name);
create index idx_ts_code
    on board_industry (ts_code);


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


create table `board_industry_hist`
(
    name        varchar(32)    null comment '概念名称',
    trade_date  varchar(8)     null comment '交易日期',
    pri_open    decimal(18, 2) null comment '开盘价',
    pri_close   decimal(18, 2) null comment '收盘价',
    pri_high    decimal(18, 2) null comment '最高价',
    pri_low     decimal(18, 2) null comment '最低价',
    pct_chg     decimal(18, 2) null comment '涨跌幅',
    am_chg      decimal(18, 2) null comment '涨跌额',
    vol         bigint         null comment '成交量（手）',
    amount      decimal(18, 2) null comment '成交额',
    amplitude   decimal(18, 2) null comment '振幅',
    change_hand decimal(18, 2) null comment '换手率',
    unit        varchar(3)     null comment 'period: 日k,周k,月k',
    adjust      varchar(3)     null comment '复权方式: qfq, hfq, 空'
) collate = utf8mb4_general_ci;
create index idx_sdl_code
    on board_industry_hist (name);
create index idx_sdl_date
    on board_industry_hist (trade_date);
create index idx_unit
    on board_industry_hist (unit);
create index idx_adjust
    on board_industry_hist (adjust);

drop table em_real_time_etf;
create table em_real_time_etf
(
    trade_date             varchar(32)    null comment '交易日期',
    ts_code                varchar(10)    null comment '股票代码',
    name                   varchar(32)    null comment '名称',
    current_pri            decimal(18, 3) null comment '最新价',
    am_chg                 decimal(18, 3) null comment '涨跌额',
    pct_chg                decimal(18, 3) null comment '涨跌幅',
    vol                    bigint         null comment '成交量（手）',
    amount                 decimal(18, 3) null comment '成交额（千元）',
    pri_open               decimal(18, 3) null comment '开盘价',
    pri_high               decimal(18, 3) null comment '最高价',
    pri_low                decimal(18, 3) null comment '最低价',
    pri_close_pre          decimal(18, 3) null comment '昨收价',
    change_hand            decimal(18, 3) null comment '换手率',
    circulation_market_cap decimal(18, 3) null comment '流通市值',
    market_cap             decimal(18, 3) null comment '总市值'
) charset = utf8mb4;
create index inx_trade_date
    on em_real_time_etf (trade_date);
create index inx_ts_code
    on em_real_time_etf (ts_code);

-- 20230519

create table `market_fund_flow`
(
    `statics_date`     varchar(32) comment '统计日期',
    `trade_date`       varchar(32) comment '交易日期',
    `sh_pri_close`     decimal(18, 2) null comment '收盘价',
    `sh_pct_chg`       decimal(18, 2) null comment '涨跌幅',
    `sz_pri_close`     decimal(18, 2) null comment '收盘价',
    `sz_pct_chg`       decimal(18, 2) null comment '涨跌幅',
    `main_fund`        decimal(18, 2) null comment '主力净流入-净额',
    `main_fund_per`    decimal(18, 2) null comment '主力净流入-净占比',
    `larger_order`     decimal(18, 2) null comment '超大单-净额',
    `larger_order_per` decimal(18, 2) null comment '超大单-净占比',
    `large_order`      decimal(18, 2) null comment '大单-净额',
    `large_order_per`  decimal(18, 2) null comment '大单-净占比',
    `medium_order`     decimal(18, 2) null comment '中单-净额',
    `medium_order_per` decimal(18, 2) null comment '中单-净占比',
    `small_order`      decimal(18, 2) null comment '小单-净额',
    `small_order_per`  decimal(18, 2) null comment '小单-净占比'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

create index idx_statics_date on `market_fund_flow` (statics_date);
create index idx_trade_date on `market_fund_flow` (trade_date);


create table `individual_fund_flow`
(
    `statics_date`     varchar(32) comment '统计日期',
    `ts_code`          varchar(32) comment '代码',
    `trade_date`       varchar(32) comment '交易日期',
    `pri_close`        decimal(18, 2) null comment '收盘价',
    `pct_chg`          decimal(18, 2) null comment '涨跌幅',
    `main_fund`        decimal(18, 2) null comment '主力净流入-净额',
    `main_fund_per`    decimal(18, 2) null comment '主力净流入-净占比',
    `larger_order`     decimal(18, 2) null comment '超大单-净额',
    `larger_order_per` decimal(18, 2) null comment '超大单-净占比',
    `large_order`      decimal(18, 2) null comment '大单-净额',
    `large_order_per`  decimal(18, 2) null comment '大单-净占比',
    `medium_order`     decimal(18, 2) null comment '中单-净额',
    `medium_order_per` decimal(18, 2) null comment '中单-净占比',
    `small_order`      decimal(18, 2) null comment '小单-净额',
    `small_order_per`  decimal(18, 2) null comment '小单-净占比'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

create index idx_statics_date on `individual_fund_flow` (statics_date);
create index idx_ts_code on `individual_fund_flow` (ts_code);
create index idx_trade_date on `individual_fund_flow` (trade_date);


-- 20230523

create table `fund_flow_rank`
(
    `statics_date`     varchar(32) comment '统计日期',
    `name`             varchar(32) comment '代码',
    `pct_chg`          decimal(18, 2) null comment '涨跌幅',
    `main_fund`        decimal(18, 2) null comment '主力净流入-净额',
    `main_fund_per`    decimal(18, 2) null comment '主力净流入-净占比',
    `larger_order`     decimal(18, 2) null comment '超大单-净额',
    `larger_order_per` decimal(18, 2) null comment '超大单-净占比',
    `large_order`      decimal(18, 2) null comment '大单-净额',
    `large_order_per`  decimal(18, 2) null comment '大单-净占比',
    `medium_order`     decimal(18, 2) null comment '中单-净额',
    `medium_order_per` decimal(18, 2) null comment '中单-净占比',
    `small_order`      decimal(18, 2) null comment '小单-净额',
    `small_order_per`  decimal(18, 2) null comment '小单-净占比',
    `max_name`         varchar(32) comment '净流入最大股',
    `indicator`        varchar(16) comment '今日, 5日, 10日',
    `sector`           varchar(16) comment '行业资金流, 概念资金流, 地域资金流'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

create index inx_statics_date
    on fund_flow_rank (statics_date);
create index inx_name
    on fund_flow_rank (name);
create index inx_indicator
    on fund_flow_rank (indicator);
create index inx_sector
    on fund_flow_rank (sector);

-- 20230614
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

-- 20230703
alter table roc_model
    add index r_idx (`ts_code`, `create_time`);

explain
select r.*
from roc_model r
where (select count(*)
       from roc_model
       where ts_code = r.ts_code
         and ts_code in
             ('603185', '002038', '600060', '300343', '603267', '600110', '603611', '600150', '002380', '000626',
              '603773',
              '603496', '603859', '600776', '002437', '002290', '002600', '603313', '603699', '603220', '000813'
                 )
         and create_time = '2023-07-03 12:15:47'
         and end_date > r.end_date) < 2
  and r.ts_code in ('603185', '002038', '600060', '300343', '603267', '600110', '603611', '600150', '002380', '000626',
                    '603773',
                    '603496', '603859', '600776', '002437', '002290', '002600', '603313', '603699', '603220', '000813'
    )
  and r.create_time = '2023-07-03 12:15:47'
order by r.ts_code, end_date desc;

-- 20230706
alter table roc_model
    add index r_pa_idx (`ts_code`, `params`);

-- 20230707
alter table `em_d_a_stock`
    add index `u_code_date` (`ts_code`, `trade_date`);

-- 20230714
create table `em_achievement`
(
    trade_date                varchar(32)    null comment '交易日期',
    ts_code                   varchar(10)    null comment '股票代码',
    name                      varchar(8)     null comment '名称',
    earnings_per_share        decimal(18, 2) null comment '每股收益',
    operating_income          decimal(18, 2) null comment '营业收入',
    operating_income_yoy      decimal(18, 2) null comment '营业收入-同比增长(year on year)',
    operating_income_qoq      decimal(18, 2) null comment '营业收入-环比增长(quarter on quarter)',
    net_profit                decimal(18, 2) null comment '净利润',
    net_profit_yoy            decimal(18, 2) null comment '净利润-同比增长',
    net_profit_qoq            decimal(18, 2) null comment '净利润-环比增长',
    net_asset_value_per_share decimal(18, 2) null comment '每股净资产',
    roe                       decimal(18, 2) null comment '净资产收益率 Return on equity',
    cash_flow_per_share       decimal(18, 2) null comment '每股经营现金流量',
    gross_profits             decimal(18, 2) null comment '销售毛利率',
    industry                  varchar(32)    null comment '所属行业',
    newest_board_date         varchar(16) comment '最新公告日期'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;
create index idx_trade_date on `em_achievement` (trade_date);
create index idx_ts_code on `em_achievement` (ts_code);

alter table `em_achievement`
    add column `statistic_date` varchar(16) comment '统计日期';

-- 20230719
create table concern_code
(
    `sn`          int primary key auto_increment,
    `ts_code`     varchar(32) comment 'code',
    `trade_date`  varchar(32) comment 'trade_date',
    `create_time` datetime comment '创建日期'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

alter table `concern_code`
    add column `remark` varchar(64) comment '备注';

-- 20230727
alter table `roc_model`
    add index i_roc_param (`params`);

-- 20230731
alter table roc_model
    add index idx_roc_ct (`create_time`);

-- 20230907
create table `simulate_deal`
(
    `sn`         int primary key auto_increment,
    `ts_code`    varchar(10),
    `trade_date` varchar(8),
    `pri`        decimal(18, 2)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- 20230912
alter table `concern_code`
    add column `r1` decimal(18, 2) comment 'r1';
alter table `concern_code`
    add column `r2` decimal(18, 2) comment 'r2';
alter table `concern_code`
    add column `r3` decimal(18, 2) comment 'r3';
alter table `concern_code`
    add column `rh` decimal(18, 2) comment 'rh';

-- 20230918
alter table concern_code
    add column r0 decimal(18, 2) comment 'r0';

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
alter table continuous_up
    drop column `sort`;

alter table concern_code
    add column `pe`          decimal(18, 3) comment '市盈率(动)',
    add column `pb`          decimal(18, 3) comment '市净率',
    add column `change_hand` decimal(18, 3) comment '市净率';

-- 20231102
create table `owner_count`
(
    `statics_date`  varchar(32) comment '统计日期',
    `ts_code`       varchar(10) comment '股票代码',
    `name`          varchar(8) comment '名称',
    `pri_low`       decimal(18, 3) null comment '最低价',
    `pct_chg`       decimal(18, 3) null comment '涨跌幅',
    `count_now`     int comment '股东户数-本次',
    `count_last`    int comment '股东户数-上次',
    `count_d_value` decimal(18, 2) null comment '股东户数-增减',
    `count_d_per`   decimal(18, 2) null comment '股东户数-增减比例',
    `area_chg`      decimal(18, 2) null comment '区间涨跌幅',
    `c_day_now`     decimal(18, 2) comment '股东户数统计截止日-本次',
    `c_day_last`    decimal(18, 2) comment '股东户数统计截止日-上次',
    `avg_amount`    decimal(18, 2) comment '户均持股市值',
    `avg_vol`       decimal(18, 2) comment '户均持股数量',
    `amount`        decimal(18, 2) comment '总市值',
    `vol`           decimal(18, 2) comment '总股本',
    `public_day`    varchar(16) comment '公告日期'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

create index idx_statics_date on `owner_count` (statics_date);

alter table owner_count
    change column `pri_low` `pri_close` decimal(18, 3);

create table `dragon_tiger_rank`
(
    `statics_date`   varchar(32) comment '统计日期',
    `ts_code`        varchar(10) comment '代码',
    `name`           varchar(8) comment '名称',
    `remark`         varchar(512) comment '解读',
    `pri_close`      decimal(18, 3) null comment '收盘价',
    `pct_chg`        decimal(18, 3) null comment '涨跌幅',
    `net_buy_amount` decimal(18, 3) null comment '龙虎榜净买额',
    `buy_amount`     decimal(18, 3) null comment '龙虎榜买入额',
    `sell_amount`    decimal(18, 3) null comment '龙虎榜卖出额',
    `trade_amount`   decimal(18, 3) comment '龙虎榜成交额',
    `market_amount`  decimal(18, 3) comment '市场总成交额',
    `net_buy_per`    decimal(18, 3) comment '净买额占总成交比',
    `trade_per`      decimal(18, 3) comment '成交额占总成交比',
    `change_hand`    decimal(18, 3) comment '换手率',
    `circle_market`  decimal(18, 3) comment '流通市值',
    `reason`         varchar(512) comment '上榜原因'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

create index idx_statics_date on `dragon_tiger_rank` (statics_date);

-- 20231103

create table `unusual_action`
(
    `statics_date` varchar(32) comment '统计日期',
    `action_time`  varchar(8) comment '发生时间',
    `ts_code`      varchar(10) comment '代码',
    `name`         varchar(8) comment '名称',
    `type`         varchar(32) comment '板块',
    `remarks`      varchar(128) comment '相关信息'
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;


create index idx_statics_date on `unusual_action` (statics_date);

alter table concern_code
    add column `hr` decimal(18, 3) comment 'hand ratio';

-- 20231110
create table `em_constants`
(
    `c_key`   varchar(32) primary key,
    `c_value` varchar(256)
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

alter table em_constants add column `buy_price` decimal(18, 3) ;

