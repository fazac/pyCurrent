DROP PROCEDURE IF EXISTS `realdata`;
DROP PROCEDURE IF EXISTS `conceptdata`;
DROP PROCEDURE IF EXISTS `industrydata`;
DROP PROCEDURE IF EXISTS `realDataNew`;
DROP PROCEDURE IF EXISTS `conceptDataNew`;
DROP PROCEDURE IF EXISTS `industryDataNew`;
DROP PROCEDURE IF EXISTS `realDataOld`;
DROP PROCEDURE IF EXISTS `conceptDataOld`;
DROP PROCEDURE IF EXISTS `industryDataOld`;
DROP PROCEDURE IF EXISTS `realDataNewAll`;
DROP PROCEDURE IF EXISTS `realDataOldAll`;
DROP PROCEDURE IF EXISTS `hisdata`;
DROP PROCEDURE IF EXISTS `hiscode`;
DROP PROCEDURE IF EXISTS `conceptt`;
DROP PROCEDURE IF EXISTS `industryy`;
DROP PROCEDURE IF EXISTS `reall`;
DROP PROCEDURE IF EXISTS `realName`;
DROP PROCEDURE IF EXISTS `realDay`;
DROP PROCEDURE IF EXISTS `realsDay`;
DROP PROCEDURE IF EXISTS `rocc`;
DROP PROCEDURE IF EXISTS `rocNew`;
DROP PROCEDURE IF EXISTS `rocOld`;
DROP PROCEDURE IF EXISTS `openn`;
DROP PROCEDURE IF EXISTS `opennTime`;
DROP PROCEDURE IF EXISTS `staticss`;
DROP PROCEDURE IF EXISTS `mdd`;
DROP PROCEDURE IF EXISTS `countem`;
DROP PROCEDURE IF EXISTS `floww`;

DELIMITER $$

CREATE PROCEDURE realdata(in queryDate varchar(8))
BEGIN
    # 查询事实数据
select ts_code,
       name,
       current_pri,
       pct_chg,
       pri_high,
       pri_low,
       pri_open,
       pri_close_pre,
       change_hand,
       pe,
       pb,
       amount / vol / 100
from em_real_time_stock
where ts_code in (select ts_code
                  from concern_code
                  where concern_code.trade_date = queryDate
                    and create_time = (select max(create_time) from concern_code))
  and trade_date = (select max(trade_date) from em_real_time_stock)
order by ts_code;
END $$

CREATE PROCEDURE conceptdata(in queryDate varchar(8))
BEGIN
    # 查询事实数据
select ts_code, group_concat(distinct symbol)
from board_concept_con
where ts_code in (select ts_code
                  from concern_code
                  where concern_code.trade_date = queryDate
                    and create_time = (select max(create_time) from concern_code))
  and trade_date = (select max(trade_date) from board_concept_con)
group by ts_code
order by ts_code;
END $$

CREATE PROCEDURE industrydata(in queryDate varchar(8))
BEGIN
    # 查询事实数据
select ts_code, group_concat(distinct symbol)
from board_industry_con
where ts_code in (select ts_code
                  from concern_code
                  where concern_code.trade_date = queryDate
                    and create_time = (select max(create_time) from concern_code))
  and trade_date = (select max(trade_date) from board_industry_con)
group by ts_code
order by ts_code;
END $$


CREATE PROCEDURE realDataNew(in queryDate varchar(8))
BEGIN
    # 查询事实数据
select ts_code,
       name,
       pct_chg,
       change_hand,
       pe,
       pb,
       truncate((pri_high - pri_close_pre) * 100 / pri_close_pre, 3) as 'high',
        truncate((pri_low - pri_close_pre) * 100 / pri_close_pre, 3)  as 'low',
        truncate((pri_open - pri_close_pre) * 100 / pri_close_pre, 3) as 'open',
        pri_close_pre,
       current_pri,
       amount / vol / 100
from em_real_time_stock
where ts_code in (select ts_code
                  from concern_code
                  where concern_code.trade_date = queryDate
                    and create_time = (select max(create_time) from concern_code))
  and ts_code not in (select ts_code
                      from concern_code
                      where concern_code.trade_date < queryDate
                        and create_time = (select max(create_time) from concern_code))
  and trade_date = (select max(trade_date) from em_real_time_stock)
order by ts_code;
END $$

CREATE PROCEDURE realDataOld(in queryDate varchar(8))
BEGIN
    # 查询事实数据
select ts_code,
       name,
       pct_chg,
       change_hand,
       pe,
       pb,
       truncate((pri_high - pri_close_pre) * 100 / pri_close_pre, 3) as 'high',
        truncate((pri_low - pri_close_pre) * 100 / pri_close_pre, 3)  as 'low',
        truncate((pri_open - pri_close_pre) * 100 / pri_close_pre, 3) as 'open',
        pri_close_pre,
       current_pri,
       amount / vol / 100
from em_real_time_stock
where ts_code in (select ts_code
                  from concern_code
                  where concern_code.trade_date = queryDate
                    and create_time = (select max(create_time) from concern_code))
  and ts_code in (select ts_code
                  from concern_code
                  where concern_code.trade_date < queryDate
                    and create_time = (select max(create_time) from concern_code))
  and trade_date = (select max(trade_date) from em_real_time_stock)
order by ts_code;
END $$


CREATE PROCEDURE realDataNewAll()
BEGIN
    declare startDate varchar(8);
    declare queryDate varchar(8);
select replace(if(day(now()) > 18, DATE_ADD(LAST_DAY(NOW() - INTERVAL 1 MONTH), INTERVAL 1 DAY),
                                   DATE_ADD(LAST_DAY(DATE_SUB(Now(), INTERVAL 1 MONTH)), INTERVAL -15 DAY)), '-', '')
into startDate;

select replace(curdate(), '-', '') into queryDate;

call realDataNew(queryDate);
call industryDataNew(queryDate);
call conceptDataNew(queryDate);
call hisdata(queryDate, startDate);
call rocNew(queryDate);
#     drop temporary table if exists `ts_codes`;
#     drop temporary table if exists `ts_industry`;
#     drop temporary table if exists `ts_concept`;
#
#
#     create temporary table `ts_codes`
#     select ts_code
                              #     from concern_code
                                             #     where concern_code.trade_date = queryDate
                       #       and create_time = (select max(create_time) from concern_code)
#       and ts_code not in (
#         select ts_code
#         from concern_code
#         where concern_code.trade_date < queryDate
#           and create_time = (select max(create_time) from concern_code))
#     order by ts_code;
#
#     create temporary table `ts_industry`
#     select c.ts_code, group_concat(distinct c.symbol) as sy
                       #     from board_industry_con c,
                                  #          ts_codes s
#     where c.ts_code = s.ts_code
                       #       and c.trade_date = (select max(trade_date) from board_industry_con)
                       #     group by c.ts_code;
#
#     create temporary table `ts_concept`
#     select c.ts_code, group_concat(distinct c.symbol) as sy
                       #     from board_concept_con c,
                                  #          ts_codes s
#     where c.ts_code = s.ts_code
                       #       and c.trade_date = (select max(trade_date) from board_concept_con)
                       #     group by c.ts_code;
#
#     # 查询事实数据
#     select t.ts_code,
             #            t.name,
              #            c1.sy,
              #            c2.sy,
              #            t.change_hand,
              #            t.pct_chg,
              #            t.pe,
              #            t.pb,
              #            t.amount / t.vol / 100,
              #            t.current_pri,
              #            t.pri_close_pre,
              #            t.pri_open,
              #            t.pri_low,
              #            t.pri_high
#     from em_real_time_stock t,
           #          ts_industry c1,
#          ts_concept c2
#     where t.ts_code = c1.ts_code
          #       and t.trade_date = (select max(trade_date) from em_real_time_stock)
          #       and t.ts_code = c1.ts_code
          #       and t.ts_code = c2.ts_code
          #     order by t.ts_code;
END $$

CREATE PROCEDURE realDataOldAll()
BEGIN
    declare startDate varchar(8);
    declare queryDate varchar(8);
select replace(if(day(now()) > 18, DATE_ADD(LAST_DAY(NOW() - INTERVAL 1 MONTH), INTERVAL 1 DAY),
                                   DATE_ADD(LAST_DAY(DATE_SUB(Now(), INTERVAL 1 MONTH)), INTERVAL -15 DAY)), '-', '')
into startDate;

select replace(curdate(), '-', '') into queryDate;
call realDataOld(queryDate);
call industryDataOld(queryDate);
call conceptDataOld(queryDate);
call hisdata(queryDate, startDate);
call rocOld(queryDate);
END $$

CREATE PROCEDURE conceptDataNew(in queryDate varchar(8))
BEGIN
    # 查询事实数据
select ts_code, group_concat(distinct symbol)
from board_concept_con
where ts_code in (select ts_code
                  from concern_code
                  where concern_code.trade_date = queryDate
                    and create_time = (select max(create_time) from concern_code))
  and ts_code not in (select ts_code
                      from concern_code
                      where concern_code.trade_date < queryDate
                        and create_time = (select max(create_time) from concern_code))
  and trade_date = (select max(trade_date) from board_concept_con)
group by ts_code
order by ts_code;
END $$

CREATE PROCEDURE conceptDataOld(in queryDate varchar(8))
BEGIN
    # 查询事实数据
select ts_code, group_concat(distinct symbol)
from board_concept_con
where ts_code in (select ts_code
                  from concern_code
                  where concern_code.trade_date = queryDate
                    and create_time = (select max(create_time) from concern_code))
  and ts_code in (select ts_code
                  from concern_code
                  where concern_code.trade_date < queryDate
                    and create_time = (select max(create_time) from concern_code))
  and trade_date = (select max(trade_date) from board_concept_con)
group by ts_code
order by ts_code;
END $$


CREATE PROCEDURE industryDataNew(in queryDate varchar(8))
BEGIN
    # 查询事实数据
select ts_code, group_concat(distinct symbol)
from board_industry_con
where ts_code in (select ts_code
                  from concern_code
                  where concern_code.trade_date = queryDate
                    and create_time = (select max(create_time) from concern_code))
  and ts_code not in (select ts_code
                      from concern_code
                      where concern_code.trade_date < queryDate
                        and create_time = (select max(create_time) from concern_code))
  and trade_date = (select max(trade_date) from board_industry_con)
group by ts_code
order by ts_code;
END $$

CREATE PROCEDURE industryDataOld(in queryDate varchar(8))
BEGIN
    # 查询事实数据
select ts_code, group_concat(distinct symbol)
from board_industry_con
where ts_code in (select ts_code
                  from concern_code
                  where concern_code.trade_date = queryDate
                    and create_time = (select max(create_time) from concern_code))
  and ts_code in (select ts_code
                  from concern_code
                  where concern_code.trade_date < queryDate
                    and create_time = (select max(create_time) from concern_code))
  and trade_date = (select max(trade_date) from board_industry_con)
group by ts_code
order by ts_code;
END $$


CREATE PROCEDURE hisdata(in queryDate varchar(8), in startDate varchar(8))
BEGIN
    # 查询历史数据
select ts_code,
       trade_date,
       pct_chg,
       change_hand,
       pri_high,
       pri_low,
       pri_open,
       pri_close,
       amount / vol / 100
from em_d_n_stock
where ts_code in (select ts_code
                  from concern_code
                  where concern_code.trade_date = queryDate
                    and create_time = (select max(create_time) from concern_code))
  and trade_date > startDate
order by ts_code, trade_date;
END $$

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

create procedure realsDay()
BEGIN
    declare queryCode varchar(6);
    DECLARE done INT DEFAULT FALSE;#结束标识
    declare ucursor CURSOR FOR SELECT ts_code
                               from concern_code
                               where create_time = (select max(create_time) from concern_code)
                                 and remark like '%(%'
                                 and trade_date = curdate();
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;-- 结束标识
open ucursor;
out_loop:
    LOOP
        IF done THEN-- 结束标识
            LEAVE out_loop;
END IF;
FETCH ucursor into queryCode;
call realDay(queryCode);
END LOOP out_loop;
close ucursor;
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

create procedure rocNew(in queryDate varchar(8))
BEGIN
select *
from roc_model
where params = (select max(params) from roc_model)
  and ts_code in
      (select ts_code
       from concern_code
       where concern_code.trade_date = queryDate
         and create_time = (select max(create_time) from concern_code))
  and ts_code not in (select ts_code
                      from concern_code
                      where concern_code.trade_date < queryDate
                        and create_time = (select max(create_time) from concern_code))
order by ts_code;

end
$$

create procedure rocOld(in queryDate varchar(8))
BEGIN
select *
from roc_model
where params = (select max(params) from roc_model)
  and ts_code in
      (select ts_code
       from concern_code
       where concern_code.trade_date = queryDate
         and create_time = (select max(create_time) from concern_code))
  and ts_code in (select ts_code
                  from concern_code
                  where concern_code.trade_date < queryDate
                    and create_time = (select max(create_time) from concern_code))
order by ts_code;
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
    SET @sql = CONCAT(' select t.ts_code,t.name,t.pct_chg,t.change_hand, truncate(t.change_hand/s.change_hand,3) as hr,
           t.pe,t.pb,truncate(t.circulation_market_cap/100000000,2) as cap,
           truncate((t.pri_open - t.pri_close_pre) * 100 / t.pri_close_pre, 3) as ''open'',
           truncate((t.pri_low - t.pri_close_pre) * 100 / t.pri_close_pre, 3)  as ''low'',
           truncate((t.pri_high - t.pri_close_pre) * 100 / t.pri_close_pre, 3) as ''high'',
           t.current_pri,
           truncate(t.amount / t.vol / 100, 3)                               as ''avg_pri''
    from em_real_time_stock t, em_d_n_stock s
    where t.trade_date = (select max(trade_date) from em_real_time_stock)
      and t.ts_code in (', queryCondition,
                      ')
      and t.ts_code = s.ts_code
      and s.trade_date = (select max(trade_date) from em_d_n_stock)
    order by t.ts_code ;');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
end
$$

CREATE PROCEDURE opennTime(in queryCodes varchar(512), in queryDate varchar(19))
BEGIN
    DECLARE queryCondition VARCHAR(512);
    SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ',', '\',\''), '\'');
    SET @sql = CONCAT(' select ts_code,name,pct_chg,change_hand,current_pri,
           truncate(amount / vol / 100, 3)                               as ''avg_pri'',
           truncate((pri_open - pri_close_pre) * 100 / pri_close_pre, 3) as ''open'',
           truncate((pri_low - pri_close_pre) * 100 / pri_close_pre, 3)  as ''low'',
           truncate((pri_high - pri_close_pre) * 100 / pri_close_pre, 3) as ''high''
    from em_real_time_stock
    where trade_date = \'', queryDate, '\'
      and ts_code in (', queryCondition,
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

select replace(if(day(now()) > 18, DATE_ADD(LAST_DAY(NOW() - INTERVAL 1 MONTH), INTERVAL 1 DAY),
                                   DATE_ADD(LAST_DAY(DATE_SUB(Now(), INTERVAL 1 MONTH)), INTERVAL -15 DAY)), '-', '')
into startDate;

if queryCodes like '%,%' then
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ',', '\',\''), '\'');
else
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ' ', '\',\''), '\'');
end if;

    #     SET @sql = CONCAT('select concat(''|'', t.name, ''|'', t.ts_code, ''|'', t.pct_chg,
#               ''|'', t.change_hand,''|'', truncate(t.change_hand/s.change_hand,3), ''|'',t.pe, ''|'',t.pb, ''|'',
#               truncate((t.pri_open - t.pri_close_pre) * 100 / t.pri_close_pre, 3), ''|'',
#               truncate((t.pri_low - t.pri_close_pre) * 100 / t.pri_close_pre, 3), ''|'',
#               truncate((t.pri_high - t.pri_close_pre) * 100 / t.pri_close_pre, 3), ''|'') as \'|名称|代码|当前|hand|hr|pe|pb|开|低|高|\'
#     from em_real_time_stock t, em_d_n_stock s
#     where t.trade_date = (select max(trade_date) from em_real_time_stock)
#       and t.ts_code in (', queryCondition,
#                       ')
#       and t.ts_code = s.ts_code
#       and s.trade_date = (select max(trade_date) from em_d_n_stock where trade_date !=replace(curdate(),''-'',''''))
#     order by t.ts_code ;');
#
#
#     PREPARE stmt FROM @sql;
#     EXECUTE stmt;
#     DEALLOCATE PREPARE stmt;

call openn(queryCodes);
call industryy(queryCodes);
call conceptt(queryCodes);
call hiscode(queryCodes, startDate);
call rocc(queryCodes);
end
$$

CREATE PROCEDURE mdd(in queryCodes varchar(512))
BEGIN
    DECLARE queryCondition VARCHAR(512);
    declare startDate varchar(8);

select replace(if(day(now()) > 18, DATE_ADD(LAST_DAY(NOW() - INTERVAL 1 MONTH), INTERVAL 1 DAY),
                                   DATE_ADD(LAST_DAY(DATE_SUB(Now(), INTERVAL 1 MONTH)), INTERVAL -15 DAY)), '-', '')
into startDate;

if queryCodes like '%,%' then
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ',', '\',\''), '\'');
else
        SET queryCondition = CONCAT('\'', REPLACE(queryCodes, ' ', '\',\''), '\'');
end if;

    SET @sql = CONCAT('select concat(''|'', t.name, ''|'', t.ts_code, ''|'', t.pct_chg,
              ''|'', t.change_hand,''|'', truncate(t.change_hand/s.change_hand,3), ''|'',t.pe, ''|'',t.pb, ''|'',
              truncate((t.pri_open - t.pri_close_pre) * 100 / t.pri_close_pre, 3), ''|'',
              truncate((t.pri_low - t.pri_close_pre) * 100 / t.pri_close_pre, 3), ''|'',
              truncate((t.pri_high - t.pri_close_pre) * 100 / t.pri_close_pre, 3), ''|'') as \'|名称|代码|当前|hand|hr|pe|pb|开|低|高|\'
    from em_real_time_stock t, em_d_n_stock s
    where t.trade_date = (select max(trade_date) from em_real_time_stock)
      and t.ts_code in (', queryCondition,
                      ')
      and t.ts_code = s.ts_code
      and s.trade_date = (select max(trade_date)
from (select distinct trade_date from em_d_n_stock order by trade_date desc limit 2) t
where trade_date != replace(curdate(), ''-'', ''''))
    order by t.ts_code ;');


PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

end
$$

create procedure countem()
begin
    declare startDate varchar(8);
select replace(DATE_SUB(curdate(), INTERVAL 30 DAY), '-', '')
into startDate;
select trade_date, count(1)
from em_d_n_stock
where trade_date > startDate
group by trade_date
order by trade_date desc;
select trade_date, count(1)
from em_d_a_stock
where trade_date > startDate
group by trade_date
order by trade_date desc;
select trade_date, count(1)
from em_d_b_stock
where trade_date > startDate
group by trade_date
order by trade_date desc;
end
$$

create procedure floww(in queryCode varchar(6))
BEGIN
select statics_date,
       trade_date,
       main_fund + large_order + medium_order + small_order as sum,
           main_fund,
           large_order,
           medium_order,
           small_order
from individual_fund_flow
where ts_code = queryCode
  and statics_date = (select max(statics_date) from individual_fund_flow)
order by trade_date desc;
end
$$

DELIMITER ;

#
#
# CALL realdata('20231019');
# CALL conceptdata('20231019');
# CALL hisdata('20231019', '20230915');
# CALL reall('300180');
# CALL conceptt('300180');
# CALL industryy('300180');
# CALL openn('300180');
