package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.entity.pk.BasicStockPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmRealTimeStockRepo extends JpaRepository<EmRealTimeStock, BasicStockPK> {
    @Query(value = """
            select t.*
            from em_real_time_stock t
            where t.trade_date = (select (max(a.trade_date)) from em_real_time_stock a)
            order by t.ts_code;""", nativeQuery = true)
    List<EmRealTimeStock> findLast();

    @Query(value = """
            select distinct trade_date from em_real_time_stock where trade_date>curdate() order by trade_date;
            """, nativeQuery = true)
    List<String> findTradeDates();

    @Query(value = """
            select * from em_real_time_stock where trade_date = :tradeDate order by ts_code;
            """, nativeQuery = true)
    List<EmRealTimeStock> findStockByDate(@Param("tradeDate") String tradeDate);

    @Query(value = """
            select * from em_real_time_stock where ts_code = :tsCode and trade_date >curdate() order by trade_date;
            """, nativeQuery = true)
    List<EmRealTimeStock> findStocksByCode(@Param("tsCode") String tsCode);


}
