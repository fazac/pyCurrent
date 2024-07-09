package com.stock.pycurrent.repo;


import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.LastHandPri;
import com.stock.pycurrent.entity.pk.BasicStockPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
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

    @Query(value = "select * from em_d_n_stock where trade_date >=:tradeDate and ts_code = :tsCode", nativeQuery = true)
    List<EmDNStock> findOverDate(@Param("tradeDate") String tradeDate, @Param("tsCode") String tsCode);

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

    @Query(value = """
            with st as (select trade_date,
                               ts_code,
                               pri_close,
                               ROW_NUMBER() over (order by trade_date desc)     as sn,
                               sum(change_hand) over (order by trade_date desc) as sh,
                               round(sum(amount) over (order by trade_date desc) / sum(vol) over (order by trade_date desc) / 100,
                                     2)                                         as sa
                        from em_d_n_stock
                        where ts_code = :code and trade_date <= :tradeDate
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
                        """, nativeQuery = true)
    List<Object[]> findLastHandPri(@Param("code") String code, @Param("tradeDate") String tradeDate);


    @Query(value = "select distinct trade_date from em_d_n_stock where trade_date > '20240101' and trade_date < '20240708' order by  trade_date  ", nativeQuery = true)
    List<String> findIntraYearDate();

    @Query(value = """
                select max(trade_date)
                    from (select trade_date,
                                 sum(change_hand) over (order by trade_date desc) as sh
                          from em_d_n_stock
                          where ts_code = :code
                            and trade_date <= '20240101'
                          order by trade_date desc) t
                    where t.sh > 100;
            """, nativeQuery = true)
    String findMinTradeDate(@Param("code") String code);
}
