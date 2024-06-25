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

    call prepare_codes(queryCodes, queryCondition);

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

    DECLARE element VARCHAR(8);
    DECLARE cmd CHAR(255);
    call prepare_codes(queryCodes, queryCondition);


    set @tableName = CONCAT('em_real_time_stock', '_', DATE_FORMAT(CURDATE(), '%Y%m%d'));
    WHILE queryCondition != ''
        DO
            SET element = SUBSTRING_INDEX(queryCondition, delim, 1);
            set @sql = concat(
                    'select left(right(r.trade_date,8),5) as `',
                    concat(SUBSTRING(element, 2, 1), SUBSTRING(element, 4, 4)),
                    '`,  r.current_pri as cp, r.pct_chg as pct ,round(b.bar * 1000,1) as bar, r.change_hand as h , r.vol as v, round(r.amount / r.vol / 100, 2) as ap from ',
                    @tableName,
                    ' r left join real_bar b on  r.ts_code = b.ts_code and r.trade_date = b.trade_date where r.ts_code =',
                    element,
                    ' and r.trade_date > CURDATE() order by r.trade_date desc;');
            PREPARE stmt FROM @sql;
            EXECUTE stmt;
            DEALLOCATE PREPARE stmt;
            SET queryCondition = SUBSTRING(queryCondition, LENGTH(element) + 2);
            SET cmd = concat('python C:/Users/fa/Desktop/py/data2chart.py --emcode=', SUBSTRING(element, 2, 6));
        END WHILE;
end
$$


CREATE PROCEDURE openn(in queryCodes varchar(512))
BEGIN
    DECLARE queryCondition VARCHAR(512);
    call prepare_codes(queryCodes, queryCondition);


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
    call prepare_codes(queryCodes, queryCondition);

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

    call prepare_codes(queryCodes, queryCondition);

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

    call prepare_codes(queryCodes, queryCondition);
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

    call prepare_codes(queryCodes, queryCondition);
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


    call openn(queryCodes);
    call realDay(queryCodes);
    call industryy(queryCodes);
    call conceptt(queryCodes);
    call hiscode(queryCodes, startDate);
    call rocc(queryCodes);
#     call rbar(queryCodes);
end
$$

# CREATE PROCEDURE rbar(in queryCodes varchar(512))
# BEGIN
#     DECLARE queryCondition VARCHAR(512);
#
#     if length(queryCodes) = 5 then
#         set queryCodes = concat(left(queryCodes, 1), '0', substr(queryCodes, 2));
#     end if;
#
#     if queryCodes like '%,%' then
#         SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ',', '\',\''), '\'');
#     else
#         SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ' ', '\',\''), '\'');
#     end if;
#
#     SET @sql = CONCAT('select trade_date,ts_code,bar from real_bar where ts_code in (', queryCondition,
#                       ') and trade_date > curdate() order by ts_code, trade_date desc;');
#     PREPARE stmt FROM @sql;
#     EXECUTE stmt;
#     DEALLOCATE PREPARE stmt;
# end
$$

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
    declare 30a varchar(4);
    declare 60a varchar(4);
    declare 00a varchar(4);
    select c_30a from cur_count order by trade_date desc limit 1 into 30a;
    select c_60a from cur_count order by trade_date desc limit 1 into 60a;
    select c_00a from cur_count order by trade_date desc limit 1 into 00a;
    SET @sql = CONCAT('
                        select c_30_5u  as 5u,
                               c_30_35u as 35u,
                               c_30_13u as 13u,
                               c_30_01u as 01u,
                               c_30_01d as 10d,
                               c_30_13d as 31d,
                               c_30_37d as 73d,
                               c_30_7d  as 7d,
                               c_30u    as `', 30a, '`,
                               c_60_5u  as 65u,
                               c_60_7d  as 67d,
                               c_60u    as `', 60a, '`,
                               c_00_5u  as 05u,
                               c_00_7d  as 07d,
                               c_00u    as `', 00a, '`
                        from cur_count
                        where trade_date > curdate()
                        order by trade_date desc;
    ');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

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

    drop table if exists `tmp_sum`;

    select min(trade_date)
    from (select distinct trade_date from em_d_n_stock order by trade_date desc limit trueDayCount) t
    into startDate;

    create temporary table tmp_sum as (select *
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
                                         and hand > handLimit);

    select * from tmp_sum;

    select group_concat(ts_code)
    from tmp_sum
    into codes;

    set @tableName = CONCAT('em_real_time_stock', '_', DATE_FORMAT(CURDATE(), '%Y%m%d'));
    SET @sql = CONCAT(' select t.ts_code,t.name,s.pc,s.hand,t.pct_chg,t.change_hand,
           t.pe,t.pb,truncate(t.circulation_market_cap/100000000,2) as cap,
           truncate((t.pri_open - t.pri_close_pre) * 100 / t.pri_close_pre, 3) as ''open'',
           truncate((t.pri_low - t.pri_close_pre) * 100 / t.pri_close_pre, 3)  as ''low'',
           truncate((t.pri_high - t.pri_close_pre) * 100 / t.pri_close_pre, 3) as ''high'',
           t.current_pri,
           truncate(t.amount / t.vol / 100, 3)                               as ''avg_pri'',
           t.pri_close_pre as ''pri_pre''
    from ', @tableName, ' t, tmp_sum s
    where t.trade_date = (select max(trade_date) from ', @tableName, ')
      and t.ts_code in (', codes, ')
      and t.ts_code = s.ts_code
    order by t.ts_code ;');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

    drop table `tmp_sum`;

end$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `prepare_codes`;
DELIMITER $$
create procedure prepare_codes(in queryCodes varchar(512), out queryCondition varchar(512))
begin
    DECLARE tmpElement varchar(6);
    declare finalQueryCodes varchar(512);
    declare delim varchar(1);
    declare codeLength int;
    set finalQueryCodes = '';

    if queryCodes like '%,%' then
        set delim = ',';
    else
        set delim = ' ';
    end if;
    WHILE queryCodes != ''
        DO
            SET tmpElement = SUBSTRING_INDEX(queryCodes, delim, 1);
            set codeLength = LENGTH(tmpElement);
            if length(tmpElement) = 5 then
                set tmpElement = concat(left(tmpElement, 1), '0', substr(tmpElement, 2));
            ELSEIF length(tmpElement) = 4 then
                set tmpElement = concat('30', tmpElement);
            end if;
            set finalQueryCodes = concat(finalQueryCodes, ',\'', tmpElement, '\'');
            SET queryCodes = SUBSTRING(queryCodes, codeLength + 2);
        END WHILE;
    SET queryCondition = SUBSTRING(finalQueryCodes, 2);
end$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `realrank`;
DELIMITER $$
create procedure `realrank`()
begin

    set @tableName = CONCAT('em_real_time_stock', '_', DATE_FORMAT(CURDATE(), '%Y%m%d'));
    SET @sql = CONCAT('select ts_code, name, round(circulation_market_cap / 100000000, 1) as ci, pct_chg, change_hand, pe, pb
    from ', @tableName,
                      ' where trade_date = (select max(trade_date) from ', @tableName, ')');
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;

end$$
DELIMITER ;

