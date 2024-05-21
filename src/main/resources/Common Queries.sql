DROP PROCEDURE IF EXISTS `reall`;
DROP PROCEDURE IF EXISTS `realName`;
DROP PROCEDURE IF EXISTS `realDay`;
DROP PROCEDURE IF EXISTS `openn`;
DROP PROCEDURE IF EXISTS `staticss`;
DROP PROCEDURE IF EXISTS `industryy`;
DROP PROCEDURE IF EXISTS `conceptt`;
DROP PROCEDURE IF EXISTS `hiscode`;
DROP PROCEDURE IF EXISTS `rocc`;
DROP PROCEDURE IF EXISTS `rbar`;
DROP PROCEDURE IF EXISTS `lmtt`;
DROP PROCEDURE IF EXISTS `cptt`;
DROP PROCEDURE IF EXISTS `cptr`;
DROP PROCEDURE IF EXISTS `cdday`;

DELIMITER $$
create procedure reall(in queryCodes varchar(512))
BEGIN
    DECLARE queryCondition VARCHAR(512);

    if length(queryCodes) = 5 then
        set queryCodes = concat(left(queryCodes, 1), '0', substr(queryCodes, 2));
    end if;

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

    if length(queryCodes) = 5 then
        set queryCodes = concat(left(queryCodes, 1), '0', substr(queryCodes, 2));
    end if;

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
                    'select trade_date, ts_code, current_pri, pct_chg, change_hand, vol, round(amount / vol / 100, 2) as avg_pri from ',
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
    declare startDate varchar(8);

    select replace(if(day(now()) > 18, DATE_ADD(LAST_DAY(NOW() - INTERVAL 2 MONTH), INTERVAL 1 DAY),
                      DATE_ADD(LAST_DAY(DATE_SUB(Now(), INTERVAL 1 MONTH)), INTERVAL -45 DAY)), '-', '')
    into startDate;

    if length(queryCodes) = 5 then
        set queryCodes = concat(left(queryCodes, 1), '0', substr(queryCodes, 2));
    end if;

    call openn(queryCodes);
    call realDay(queryCodes);
    call industryy(queryCodes);
    call conceptt(queryCodes);
    call hiscode(queryCodes, startDate);
    call rocc(queryCodes);
    call rbar(queryCodes);
end
$$

CREATE PROCEDURE rbar(in queryCodes varchar(512))
BEGIN
    DECLARE queryCondition VARCHAR(512);

    if length(queryCodes) = 5 then
        set queryCodes = concat(left(queryCodes, 1), '0', substr(queryCodes, 2));
    end if;

    if queryCodes like '%,%' then
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ',', '\',\''), '\'');
    else
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ' ', '\',\''), '\'');
    end if;

    SET @sql = CONCAT('select trade_date,ts_code,bar from real_bar where ts_code in (', queryCondition,
                      ') and trade_date > curdate() order by ts_code, trade_date desc;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
end $$

CREATE PROCEDURE lmtt(in queryDate varchar(8))
BEGIN
    DECLARE queryCondition text;
    select code_value from limit_code where trade_date = queryDate into queryCondition;
    SET @sql = CONCAT('select * from json_table(\'', queryCondition,
                      '\', ''$[*]''
                COLUMNS (codet VARCHAR(6) PATH ''$.code'' DEFAULT ''1'' ON EMPTY,countt int PATH ''$.count'' DEFAULT ''1'' ON EMPTY)) AS tt;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
end $$

CREATE PROCEDURE cptt(in queryName varchar(16))
BEGIN
    SET @sql = CONCAT('select * from board_concept_con where symbol like \'%', queryName,
                      '%\' and trade_date = (select max(trade_date) from board_concept_con);');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
end $$

CREATE PROCEDURE cptr(in queryName varchar(16))
BEGIN
    set @tableName = CONCAT('em_real_time_stock', '_', DATE_FORMAT(CURDATE(), '%Y%m%d'));
    SET @sql = CONCAT('select *
    from ', @tableName, '
    where trade_date = (select max(trade_date) from ', @tableName, ')
      and ts_code in (select ts_code
                      from board_concept_con
                      where symbol like \'%', queryName, '%\'
                        and trade_date = (select max(trade_date) from board_concept_con));');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
end $$

CREATE PROCEDURE cdday()
BEGIN
    select trade_date, count(1) from em_d_n_stock group by trade_date order by trade_date desc limit 10;
    select trade_date, count(1) from em_d_a_stock group by trade_date order by trade_date desc limit 10;
end $$

DELIMITER ;


DROP PROCEDURE IF EXISTS `curcc`;
DELIMITER $$
CREATE PROCEDURE curcc()
BEGIN
    select c_30_5u,
           c_30_7d,
           c_30u,
           c_30a,
           c_60_5u,
           c_60_7d,
           c_60u,
           c_60a,
           c_00_5u,
           c_00_7d,
           c_00u,
           c_00a
    from cur_count
    where trade_date > curdate()
    order by trade_date desc;
end $$
DELIMITER ;


DROP PROCEDURE IF EXISTS `procedure_template`;
DELIMITER $$
create procedure procedure_template()
begin

end$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `ohc`;
DELIMITER $$
create procedure ohc(in dayCount int, in handLimit int)
begin
    declare startDate varchar(8);
    declare codes varchar(512);

    select min(trade_date)
    from (select distinct trade_date from em_d_n_stock order by trade_date desc limit dayCount) t
    into startDate;

    select group_concat(ts_code)
    from (select ts_code,
                 change_hand,
                 row_number() over (partition by ts_code order by change_hand ) as num
          from em_d_n_stock
          where trade_date >= startDate
            and ts_code like '30%') t
    where t.num = 1
      and t.change_hand > handLimit
    into codes;

    call openn(codes);

end$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `opc`;
DELIMITER $$
create procedure opc(in dayCount int, in pchLimit int)
begin
    declare startDate varchar(8);
    declare codes varchar(512);
    declare trueDayCount int;

    set trueDayCount = dayCount + 1;
    select min(trade_date)
    from (select distinct trade_date from em_d_n_stock order by trade_date desc limit trueDayCount) t
    into startDate;

    select group_concat(ts_code)
    from (select ts_code, round((ep - sp) * 100 / sp, 2) as pc
          from (select ts_code,
                       max(case when t1.num = 1 then t1.pri_close end)            as sp,
                       max(case when t1.num = trueDayCount then t1.pri_close end) as ep
                from (select ts_code,
                             pri_close,
                             row_number() over (partition by ts_code order by trade_date) as num
                      from em_d_n_stock
                      where trade_date >= startDate
                        and ts_code like '30%') t1
                where t1.num = 1
                   or t1.num = trueDayCount
                group by ts_code) t2) t3
    where t3.pc > pchLimit
    into codes;

    call openn(codes);

end$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `ophc`;
DELIMITER $$
create procedure ophc(in dayCount int, in pchLimit int, in handLimit int)
begin
    declare startDate varchar(8);
    declare codes varchar(512);

    declare trueDayCount int;

    set trueDayCount = dayCount + 1;

    select min(trade_date)
    from (select distinct trade_date from em_d_n_stock order by trade_date desc limit trueDayCount) t
    into startDate;

    select group_concat(ts_code)
    from (select ts_code, round((ep - sp) * 100 / sp, 2) as pc, hand
          from (select ts_code,
                       max(case when t1.num = 1 then t1.pri_close end)            as sp,
                       max(case when t1.num = trueDayCount then t1.pri_close end) as ep,
                       sum(case when t1.num != 1 then t1.change_hand end)         as hand
                from (select ts_code,
                             pri_close,
                             change_hand,
                             row_number() over (partition by ts_code order by trade_date) as num
                      from em_d_n_stock
                      where trade_date >= startDate
                        and ts_code like '30%') t1
                group by ts_code) t2) t3
    where t3.pc > pchLimit
      and hand > handLimit
    into codes;

    call openn(codes);

end$$
DELIMITER ;
