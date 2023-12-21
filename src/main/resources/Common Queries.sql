DROP PROCEDURE IF EXISTS `reall`;
DROP PROCEDURE IF EXISTS `realName`;
DROP PROCEDURE IF EXISTS `realDay`;


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

DELIMITER ;

