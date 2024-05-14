package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.util.DateUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.apachecommons.CommonsLog;
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
        String sql = " select * from " + tableName + " where trade_date = (select max(a.trade_date) from " + tableName + " a) order by ts_code;";
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
        return (List<EmRealTimeStock>) entityManager.createNativeQuery(sql, EmRealTimeStock.class)
                .setParameter("tradeDate", tradeDate)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<EmRealTimeStock> findRBarStockByCode(String tsCode) {
        String tableName = "em_real_time_stock_" + DateUtils.now();
        String sql = "select * from " + tableName
                     + " where ts_code = :tsCode and trade_date > concat(CURDATE(),' 09:29:30') order by trade_date;";
        return (List<EmRealTimeStock>) entityManager.createNativeQuery(sql, EmRealTimeStock.class)
                .setParameter("tsCode", tsCode)
                .getResultList();
    }

}
