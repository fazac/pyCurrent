package com.stock.pycurrent.repo;


import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.pk.BasicStockPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface EmDNStockRepo extends JpaRepository<EmDNStock, BasicStockPK> {
    @Query(value = "select max(c.trade_date) from em_d_n_stock c", nativeQuery = true)
    String findMaxTradeDate();

    @Query(value = "select count(1) from em_d_n_stock where trade_date =:tradeDate", nativeQuery = true)
    Long findCountByDate(@Param("tradeDate") String tradeDate);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from em_d_n_stock where trade_date = :tradeDate", nativeQuery = true)
    void deleteByDate(@Param("tradeDate") String tradeDate);

    @Query(value = "select * from em_d_n_stock where trade_date=:tradeDate", nativeQuery = true)
    List<EmDNStock> findCurrent(@Param("tradeDate") String tradeDate);

    @Query(value = "select * from em_d_n_stock where ts_code = :tsCode and trade_date <:tradeDate order by trade_date desc limit 1"
            , nativeQuery = true)
    EmDNStock findLeftOne(@Param("tsCode") String tsCode, @Param("tradeDate") String tradeDate);

    @Query(value = "select * from em_d_n_stock where ts_code = :code order by trade_date desc limit :count ", nativeQuery = true)
    List<EmDNStock> findByCodeCount(@Param("code") String code, @Param("count") int count);

    @Query(value = "select min(t.trade_date) from (select distinct trade_date from em_d_n_stock order by trade_date desc limit :count) t", nativeQuery = true)
    String findMinTradeDateByCount(@Param("count") Integer count);

    @Query(value = """
                select ts_code, hand, c
                from (select ts_code,
                             sum(change_hand)                                              as hand,
                             round((max(pri_high) - min(pri_low)) / min(pri_low) * 100, 2) as c
                      from em_d_n_stock
                      where trade_date >= :tradeDate
                      group by ts_code) t1
                where (hand >= :hand or :hand is null)
                  and (c >= :pch or :pch is null);
            """, nativeQuery = true)
    List<Object> findOPHC(@Param("tradeDate") String tradeDate, @Param("hand") BigDecimal hand, @Param("pch") BigDecimal pch);

    @Query(value = """
                    select t2.ts_code,
                           round((t2.epc - t2.spc) / t2.spc * 100, 2) as rpc,
                           round((t2.epa - t2.spa) / t2.spa * 100, 2) as rpa
                    from (select ts_code,
                                 max(case when t1.rn = 1 then t1.pc end) as spc,
                                 max(case when t1.rn = :count then t1.pc end) as epc,
                                 max(case when t1.rn = 1 then t1.pa end) as spa,
                                 max(case when t1.rn = :count then t1.pa end) as epa
                          from (select e.ts_code,
                                       pri_close                                                    as pc,
                                       round(amount / vol / 100, 3)                                 as pa,
                                       trade_date,
                                       row_number() over (partition by ts_code order by trade_date) as rn
                                from em_d_n_stock e
                                where trade_date >= :tradeDate
                                  and e.ts_code in (:codes)) t1
                          group by ts_code) t2;
            """, nativeQuery = true)
    List<Object> findStatisticByCodes(@Param("codes") List<String> codes, @Param("tradeDate") String tradeDate, @Param("count") Integer count);
}
