package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.entity.emum.PyFuncEnum;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.entity.vo.OpenVO;
import com.stock.pycurrent.util.DateUtils;
import com.stock.pycurrent.util.PythonScriptUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CommonsLog
public class EmRealTimeStockService {
    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<EmRealTimeStock> findLast() {
        String tableName = "em_real_time_stock_" + DateUtils.now();
        String sql = " select * from " + tableName + " where trade_date = (select (max(a.trade_date)) from " + tableName + " a) order by ts_code;";
        return (List<EmRealTimeStock>) entityManager.createNativeQuery(sql, EmRealTimeStock.class).getResultList();
    }

    public List<EmRealTimeStock> findLastHundred() {
        String tableName = "em_real_time_stock_" + DateUtils.now();
        String sql = " select * from " + tableName + " where trade_date = (select (max(a.trade_date)) from " + tableName + " a) " +
                " and current_pri is not null order by pct_chg desc limit 100;";
        return (List<EmRealTimeStock>) entityManager.createNativeQuery(sql, EmRealTimeStock.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<String> findTradeDates() {
        String tableName = "em_real_time_stock_" + DateUtils.now();
        String sql = "select distinct trade_date from " + tableName + " where trade_date>curdate() order by trade_date;";
        return (List<String>) entityManager.createNativeQuery(sql, String.class).getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<EmRealTimeStock> findStockByDate(String tradeDate) {
        String tableName = "em_real_time_stock_" + DateUtils.now();
        String sql = "select * from " + tableName + " where trade_date = :tradeDate order by ts_code;";
        return (List<EmRealTimeStock>) entityManager.createNativeQuery(sql, EmRealTimeStock.class).setParameter("tradeDate", tradeDate).getResultList();
    }

    public List<EmRealTimeStock> findEmCurrent() {
//        return PythonScriptUtils.execThreadPY(Constants.AKSHARE_EM_REALTIME, PyFuncEnum.EM_CURRENT.toString(), this::findLast);
        return findLast();
    }

    @SuppressWarnings("unchecked")
    public List<EmRealTimeStock> findRBarStockByCode(String tsCode) {
        String tableName = "em_real_time_stock_" + DateUtils.now();
        String sql = "select * from " + tableName
                + " where ts_code = :tsCode and trade_date > concat(CURDATE(),' 09:29:30') and current_pri is not null order by trade_date;";
        return (List<EmRealTimeStock>) entityManager.createNativeQuery(sql, EmRealTimeStock.class)
                .setParameter("tsCode", tsCode)
                .getResultList();
    }

    public int findRBarStockCountByCode(String tsCode) {
        String tableName = "em_real_time_stock_" + DateUtils.now();
        String sql = "select count(1) from " + tableName
                + " where ts_code = :tsCode and trade_date > concat(CURDATE(),' 09:29:30') order by trade_date;";
        return ((Long) entityManager.createNativeQuery(sql)
                .setParameter("tsCode", tsCode)
                .getSingleResult()).intValue();
    }

    public List<EmRealTimeStock> findOpenByCode(String tsCode) {
        String tableName = "em_real_time_stock_" + DateUtils.now();
        String sql = "select * " +
                "    from " + tableName
                + " t where t.ts_code = :tsCode and  t.trade_date = (select max(trade_date) from " + tableName + ") ";
        return entityManager.createNativeQuery(sql, EmRealTimeStock.class)
                .setParameter("tsCode", tsCode)
                .getResultList();
    }

    public List<EmRealTimeStock> findOpenByName(String name) {
        String tableName = "em_real_time_stock_" + DateUtils.now();
        String sql = "select * " +
                "    from " + tableName
                + " t where t.name like :name and t.current_pri is not null and t.trade_date = (select max(trade_date) from " + tableName + ") ";
        return entityManager.createNativeQuery(sql, EmRealTimeStock.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

    @Transactional
    @Modifying
    public void createTable() {
        String tableName = "em_real_time_stock_" + DateUtils.now();
        Long res = (Long) entityManager.createNativeQuery("""
                        SELECT count(1)
                        FROM information_schema.tables
                        WHERE table_schema = 'stockrealtime'
                          AND table_name = :tableName
                          """, Long.class)
                .setParameter("tableName", tableName)
                .getSingleResult();
        if (res > 0) {
            log.warn("已存在当日表,无需新建");
            return;
        } else {
            log.warn("创建表：" + tableName);
        }
        String sql = "CREATE TABLE IF NOT EXISTS " + '`' + tableName + '`' + """
                (
                    `trade_date`                   varchar(32)    DEFAULT NULL COMMENT '交易日期',
                    `ts_code`                      varchar(10)    DEFAULT NULL COMMENT '股票代码',
                    `name`                         varchar(8)     DEFAULT NULL COMMENT '名称',
                    `current_pri`                  decimal(18, 2) DEFAULT NULL COMMENT '最新价',
                    `pct_chg`                      decimal(18, 2) DEFAULT NULL COMMENT '涨跌幅',
                    `am_chg`                       decimal(18, 2) DEFAULT NULL COMMENT '涨跌额',
                    `vol`                          int            DEFAULT NULL COMMENT '成交量（手）',
                    `amount`                       decimal(18, 2) DEFAULT NULL COMMENT '成交额（千元）',
                    `vibration`                    decimal(18, 2) DEFAULT NULL COMMENT '振幅',
                    `pri_high`                     decimal(18, 2) DEFAULT NULL COMMENT '最高价',
                    `pri_low`                      decimal(18, 2) DEFAULT NULL COMMENT '最低价',
                    `pri_open`                     decimal(18, 2) DEFAULT NULL COMMENT '开盘价',
                    `pri_close_pre`                decimal(18, 2) DEFAULT NULL COMMENT '昨收价',
                    `vol_ratio`                    decimal(18, 2) DEFAULT NULL COMMENT '量比',
                    `change_hand`                  decimal(18, 2) DEFAULT NULL COMMENT '换手率',
                    `pe`                           decimal(18, 2) DEFAULT NULL COMMENT '市盈率(动)',
                    `pb`                           decimal(18, 2) DEFAULT NULL COMMENT '市净率',
                    `market_cap`                   decimal(18, 2) DEFAULT NULL COMMENT '总市值',
                    `circulation_market_cap`       decimal(18, 2) DEFAULT NULL COMMENT '流通市值',
                    `increase_ratio`               decimal(18, 2) DEFAULT NULL COMMENT '涨速',
                    `five_minutes_increase_ratio`  decimal(18, 2) DEFAULT NULL COMMENT '5分钟涨速',
                    `sixty_minutes_increase_ratio` decimal(18, 2) DEFAULT NULL COMMENT '60分钟涨速',
                    `current_year_ratio`           decimal(18, 2) DEFAULT NULL COMMENT '年初至今涨跌幅',
                    KEY `idx_sdl_code` (`ts_code`) USING BTREE,
                    KEY `idx_sdl_date` (`trade_date`) USING BTREE
                ) ENGINE = InnoDB
                  DEFAULT CHARSET = utf8mb4
                  COLLATE = utf8mb4_general_ci
                  ROW_FORMAT = DYNAMIC;
                                        """;
        try {
            entityManager.createNativeQuery(sql).executeUpdate();
        } catch (Exception e) {
            log.error("创建失败：" + tableName, e);
        }
    }
}
