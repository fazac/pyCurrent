DROP PROCEDURE IF EXISTS `reall`;
DROP PROCEDURE IF EXISTS `realName`;
DROP PROCEDURE IF EXISTS `realDay`;
DROP PROCEDURE IF EXISTS `openn`;


DELIMITER $$
create procedure reall(in queryCodes varchar(512))
BEGIN
    DECLARE queryCondition VARCHAR(512);

    if queryCodes like '%,%' then
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ',', '\',\''), '\'');
    else
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ' ', '\',\''), '\'');
    end if;

    SET @sql = CONCAT(' select *
    from em_real_time_stock
    where trade_date = (select max(trade_date) from em_real_time_stock) and ts_code in (', queryCondition,
                      ')  order by ts_code ;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
end $$

create procedure realName(in queryName varchar(10))
BEGIN
    SET @sql = CONCAT(' select *
    from em_real_time_stock
    where trade_date = (select max(trade_date) from em_real_time_stock) and name like \'%', queryName,
                      '%\';');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
end $$


create procedure realDay(in queryCodes varchar(512))
BEGIN
    DECLARE queryCondition VARCHAR(512);
    DECLARE delim CHAR(1) DEFAULT ',';
    DECLARE idx INT DEFAULT 1;
    DECLARE element VARCHAR(6);
    if queryCodes like '%,%' then
        SET queryCondition = queryCodes;
    else
        SET queryCondition = REPLACE(queryCodes, ' ', ',');
    end if;
    WHILE queryCondition != ''
        DO
            SET element = SUBSTRING_INDEX(queryCondition, delim, 1);
            select trade_date, ts_code, current_pri, pct_chg, change_hand, vol, amount / vol / 100 as avg_pri
            from em_real_time_stock
            where ts_code = element
              and trade_date > CURDATE()
            order by trade_date desc;
            SET queryCondition = SUBSTRING(queryCondition, LENGTH(element) + 2);
            SET idx = idx + 1;
        END WHILE;
end
$$


CREATE PROCEDURE openn(in queryCodes varchar(512))
BEGIN
    DECLARE queryCondition VARCHAR(512);
    if queryCodes like '%,%' then
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ',', '\',\''), '\'');
    else
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ' ', '\',\''), '\'');
    end if;
    SET @sql = CONCAT(' select t.ts_code,t.name,t.pct_chg,t.change_hand,
           t.pe,t.pb,truncate(t.circulation_market_cap/100000000,2) as cap,
           truncate((t.pri_open - t.pri_close_pre) * 100 / t.pri_close_pre, 3) as ''open'',
           truncate((t.pri_low - t.pri_close_pre) * 100 / t.pri_close_pre, 3)  as ''low'',
           truncate((t.pri_high - t.pri_close_pre) * 100 / t.pri_close_pre, 3) as ''high'',
           t.current_pri,
           truncate(t.amount / t.vol / 100, 3)                               as ''avg_pri'',
           t.pri_close_pre as ''pri_pre''
    from em_real_time_stock t
    where t.trade_date = (select max(trade_date) from em_real_time_stock)
      and t.ts_code in (', queryCondition,')
    order by t.ts_code ;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
end
$$

DELIMITER ;

