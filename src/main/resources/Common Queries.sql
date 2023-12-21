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

DELIMITER ;

