CREATE
    DATABASE IF NOT EXISTS stockrealtime
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_general_ci;

use
    stockrealtime;


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
    `bar`             decimal(13,4),
    KEY `idx_sdl_code` (`ts_code`) USING BTREE,
    KEY `idx_sdl_date` (`trade_date`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

select * from real_bar;