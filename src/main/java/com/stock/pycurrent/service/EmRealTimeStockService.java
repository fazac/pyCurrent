package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.entity.vo.CodeDataVO;
import com.stock.pycurrent.schedule.PrepareData;
import com.stock.pycurrent.util.DateUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<EmRealTimeStock> findCptr(String symbol) {
        String tableName = "em_real_time_stock_" + DateUtils.now();
        String sql = " select * from " + tableName + " where trade_date = (select (max(a.trade_date)) from " + tableName + " a)  and current_pri is not null and ts_code in (select ts_code from board_concept_con  where symbol like :symbol and trade_date = (select max(trade_date) from board_concept_con))";
        return (List<EmRealTimeStock>) entityManager.createNativeQuery(sql, EmRealTimeStock.class).setParameter("symbol", "%" + symbol + "%").getResultList();
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

    private EmRealTimeStock findLastOneByCode(String tsCode) {
        try {
            String tableName = "em_real_time_stock_" + DateUtils.now();
            String sql = "select * from " + tableName + " t where t.ts_code = :tsCode and  t.trade_date = (select max(trade_date) from " + tableName + ") order by trade_date desc limit 1";
            return (EmRealTimeStock) entityManager.createNativeQuery(sql, EmRealTimeStock.class)
                    .setParameter("tsCode", tsCode)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void fillCodeData(CodeDataVO codeDataVO) {
        EmRealTimeStock rt = findLastOneByCode(codeDataVO.getCode());
        if (rt != null) {
            codeDataVO.setPe(rt.getPe());
            codeDataVO.setPb(rt.getPb());
            codeDataVO.setCm(rt.getCirculationMarketCap());
            codeDataVO.setCurrentPri(rt.getCurrentPri());
            codeDataVO.setPch(rt.getPctChg());
            codeDataVO.setHand(rt.getChangeHand());
        }
        codeDataVO.setLabels(PrepareData.findLabelStr(codeDataVO.getCode()));
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

    List<Object[]> findLastHandPri(String code, String tradeDate) {
        String sql = """
                with st as (select trade_date,
                                   ts_code,
                                   pri_close,
                                   ROW_NUMBER() over (order by trade_date desc)     as sn,
                                   sum(change_hand) over (order by trade_date desc) as sh,
                                   round(sum(amount) over (order by trade_date desc) / sum(vol) over (order by trade_date desc) / 100,
                                         2)                                         as sa
                            from em_d_n_stock
                            where ts_code =""".concat(
                " \'").concat(code).concat("\' and trade_date <=\'").concat(tradeDate).concat("\' ").concat(
                """
                                    order by trade_date desc)
                        select t1.trade_date,
                               t1.ts_code,
                               t1.pri_close,
                               ifnull(round(max(case
                                                    when t1.sh < 5 and t2.sh > 5
                                                        then t1.sa + (t2.sa - t1.sa) / (t2.sh - t1.sh) * (5 - t1.sh) end),
                                            2), round(case when t1.sn = 1 then t1.sa end, 2)) as p5,
                               ifnull(round(max(case
                                                    when t1.sh < 10 and t2.sh > 10
                                                        then t1.sa + (t2.sa - t1.sa) / (t2.sh - t1.sh) * (10 - t1.sh) end),
                                            2), round(case when t1.sn = 1 then t1.sa end, 2)) as p10,
                               ifnull(round(max(case
                                                    when t1.sh < 20 and t2.sh > 20
                                                        then t1.sa + (t2.sa - t1.sa) / (t2.sh - t1.sh) * (20 - t1.sh) end),
                                            2), round(case when t1.sn = 1 then t1.sa end, 2)) as p20,
                               ifnull(round(max(case
                                                    when t1.sh < 30 and t2.sh > 30
                                                        then t1.sa + (t2.sa - t1.sa) / (t2.sh - t1.sh) * (30 - t1.sh) end),
                                            2), round(case when t1.sn = 1 then t1.sa end, 2)) as p30,
                               ifnull(round(max(case
                                                    when t1.sh < 50 and t2.sh > 50
                                                        then t1.sa + (t2.sa - t1.sa) / (t2.sh - t1.sh) * (50 - t1.sh) end),
                                            2), round(case when t1.sn = 1 then t1.sa end, 2)) as p50,
                               ifnull(round(max(case
                                                    when t1.sh < 100 and t2.sh > 100
                                                        then t1.sa + (t2.sa - t1.sa) / (t2.sh - t1.sh) * (100 - t1.sh) end),
                                            2), round(case when t1.sn = 1 then t1.sa end, 2)) as p100
                        from st t1
                                 join st t2 on t1.sn = t2.sn - 1;
                                    """);
        return entityManager.createNativeQuery(sql)
                .getResultList();
    }
}
