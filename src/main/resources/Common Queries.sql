DROP PROCEDURE IF EXISTS `reall`;
DROP PROCEDURE IF EXISTS `realName`;
DROP PROCEDURE IF EXISTS `realDay`;
DROP PROCEDURE IF EXISTS `openn`;
DROP PROCEDURE IF EXISTS `staticss`;
DROP PROCEDURE IF EXISTS `industryy`;
DROP PROCEDURE IF EXISTS `conceptt`;
DROP PROCEDURE IF EXISTS `hiscode`;
DROP PROCEDURE IF EXISTS `rocc`;

DELIMITER $$
create procedure reall(in queryCodes varchar(512))
BEGIN
    DECLARE queryCondition VARCHAR(512);

    if queryCodes like '%,%' then
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ',', '\',\''), '\'');
    else
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ' ', '\',\''), '\'');
    end if;

    set @tableName = CONCAT('em_real_time_stock', '_', DATE_FORMAT(CURDATE(), '%Y%m%d'));
    SET @sql = CONCAT(' select * from ', @tableName, ' where trade_date = (select max(trade_date) from ', @tableName,
                      ') and ts_code in (',
                      queryCondition,
                      ')  order by ts_code ;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
end $$

create procedure realName(in queryName varchar(10))
BEGIN
    set @tableName = CONCAT('em_real_time_stock', '_', DATE_FORMAT(CURDATE(), '%Y%m%d'));
    SET @sql = CONCAT(' select *
    from ', @tableName, '
    where trade_date = (select max(trade_date) from ', @tableName, ') and name like \'%', queryName,
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
    set @tableName = CONCAT('em_real_time_stock', '_', DATE_FORMAT(CURDATE(), '%Y%m%d'));
    WHILE queryCondition != ''
        DO
            SET element = SUBSTRING_INDEX(queryCondition, delim, 1);
            set @sql = concat(
                    'select trade_date, ts_code, current_pri, pct_chg, change_hand, vol, amount / vol / 100 as avg_pri from ',
                    @tableName, ' where ts_code =', element,
                    ' and trade_date > CURDATE() order by trade_date desc;');
            PREPARE stmt FROM @sql;
            EXECUTE stmt;
            DEALLOCATE PREPARE stmt;
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
    set @tableName = CONCAT('em_real_time_stock', '_', DATE_FORMAT(CURDATE(), '%Y%m%d'));
    SET @sql = CONCAT(' select t.ts_code,t.name,t.pct_chg,t.change_hand,
           t.pe,t.pb,truncate(t.circulation_market_cap/100000000,2) as cap,
           truncate((t.pri_open - t.pri_close_pre) * 100 / t.pri_close_pre, 3) as ''open'',
           truncate((t.pri_low - t.pri_close_pre) * 100 / t.pri_close_pre, 3)  as ''low'',
           truncate((t.pri_high - t.pri_close_pre) * 100 / t.pri_close_pre, 3) as ''high'',
           t.current_pri,
           truncate(t.amount / t.vol / 100, 3)                               as ''avg_pri'',
           t.pri_close_pre as ''pri_pre''
    from ', @tableName, ' t
    where t.trade_date = (select max(trade_date) from ', @tableName, ')
      and t.ts_code in (', queryCondition, ')
    order by t.ts_code ;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
end
$$

CREATE PROCEDURE hiscode(in queryCodes varchar(512), in startDate varchar(8))
BEGIN
    DECLARE queryCondition VARCHAR(512);
    if queryCodes like '%,%' then
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ',', '\',\''), '\'');
    else
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ' ', '\',\''), '\'');
    end if;
    SET @sql = CONCAT('select ts_code,
           trade_date,
           pct_chg,
           change_hand,
           pri_close,
           pri_high,
           pri_low,
           pri_open,
           amount / vol / 100
    from em_d_n_stock
    where ts_code in (', queryCondition,
                      ') and trade_date >', startDate, '
    order by ts_code, trade_date;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END $$

CREATE PROCEDURE conceptt(in queryCodes varchar(512))
BEGIN
    DECLARE queryCondition VARCHAR(512);
    if queryCodes like '%,%' then
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ',', '\',\''), '\'');
    else
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ' ', '\',\''), '\'');
    end if;
    SET @sql = CONCAT('select ts_code,group_concat(distinct symbol)
    from board_concept_con
    where ts_code in (', queryCondition,
                      ') and trade_date = (select max(trade_date) from board_concept_con) group by ts_code order by ts_code;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
end $$

CREATE PROCEDURE industryy(in queryCodes varchar(512))
BEGIN
    DECLARE queryCondition VARCHAR(512);
    if queryCodes like '%,%' then
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ',', '\',\''), '\'');
    else
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ' ', '\',\''), '\'');
    end if;
    SET @sql = CONCAT(' select ts_code,group_concat(distinct symbol)
    from board_industry_con
    where ts_code in (', queryCondition,
                      ') and trade_date = (select max(trade_date) from board_industry_con) group by ts_code order by ts_code ;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
end $$

create procedure rocc(in queryCodes varchar(512))
BEGIN
    DECLARE queryCondition VARCHAR(512);
    if queryCodes like '%,%' then
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ',', '\',\''), '\'');
    else
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ' ', '\',\''), '\'');
    end if;
    SET @sql = CONCAT(' select *
    from roc_model
    where params=(select max(params) from roc_model) and ts_code in (', queryCondition,
                      ')  order by ts_code ;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
end
$$

CREATE PROCEDURE staticss(in queryCodes varchar(512))
BEGIN
    DECLARE queryCondition VARCHAR(512);
    declare startDate varchar(8);

    select replace(if(day(now()) > 18, DATE_ADD(LAST_DAY(NOW() - INTERVAL 2 MONTH), INTERVAL 1 DAY),
                      DATE_ADD(LAST_DAY(DATE_SUB(Now(), INTERVAL 1 MONTH)), INTERVAL -45 DAY)), '-', '')
    into startDate;

    if queryCodes like '%,%' then
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ',', '\',\''), '\'');
    else
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ' ', '\',\''), '\'');
    end if;

    call openn(queryCodes);
    call industryy(queryCodes);
    call conceptt(queryCodes);
    call hiscode(queryCodes, startDate);
    call rocc(queryCodes);
end
$$

DELIMITER ;

