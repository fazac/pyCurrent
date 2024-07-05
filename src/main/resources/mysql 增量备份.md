1. 查看是否已开启

   ```
   show variables like '%log_bin%'; //check log_bin == ON
   ```
   未开启则修改 `my.cnf/my.ini` 文件

   ```
    [mysqld]
    server_id=1
    log_bin=“computername-bin”
    max_binlog_size=100M
    [mysqld]
   ```
   重启`mysql`服务
2. 添加全备份(初始备份)

   ```
   mysqldump -u root -p --flush-logs --delete-master-logs --lock-all-tables --all-databases > D:\MySQLBackup\all_databases.sql 
   exit
   ```
   ●  **--flush-logs** : initialize writing a new binary log file ●  **--delete-master-logs** : delete old binary log
   files ●  **--lock-all-tables** : lock all tables across all databases
3. 增量备份

   ```
   flush binary logs;
   show binary logs;
   cd C:\ProgramData\MySQL\MySQL Server 8.0\Data
   copy DESKTOP-J6AHKLA-bin.000020 D:\MySQLBacku
   purge binary logs to ‘DESKTOP-J6AHKLA-bin.000021’; 
   exit
   ```
4. 备份还原 临时关闭 `binlog`, 修改配置文件, 重启服务

   ```
    [mysqld]
    server_id=1
    log_bin=“computername-bin”
    skip-log-bin
    max_binlog_size=100M
    [mysqld]
   ```
   连接mysql控制台

   ```
   mysql -u root -p -o test < D:\MySQLBackup\all_databases.sql (“-o” allows you to specify one database to restore)
   mysqlbinlog D:\MySQLBackup\DESKTOP-J6AHKLA-bin.000020 | mysql -u root -p
   exit
   ```
   如果出现`Unknown command \ ? .`, 可在登录时指定字符集

   ```
   mysql -uroot -p --default-character-set=utf8mb4 database_name < databases.sql
   ```

5. 远程连接
   ```angular2html
   # zerotier ip 
   create user 'fawork'@'10.243.85.210' identified by '123456'; 
   
   grant all privileges on *.* to 'fawork'@'10.243.85.210';
   
   flush privileges;
   ```