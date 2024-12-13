package com.stock.pycurrent.repo;


import com.stock.pycurrent.entity.EmDAStock;
import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.pk.BasicStockPK;
import org.checkerframework.checker.units.qual.N;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EmDAStockRepo extends JpaRepository<EmDAStock, BasicStockPK> {
    @Query(value = "select max(c.trade_date) from em_d_a_stock c", nativeQuery = true)
    String findMaxTradeDate();

    @Query(value = "select count(1) from em_d_a_stock where trade_date =:tradeDate", nativeQuery = true)
    Long findCountByDate(@Param("tradeDate") String tradeDate);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from em_d_a_stock where trade_date = :tradeDate", nativeQuery = true)
    void deleteByDate(@Param("tradeDate") String tradeDate);

    @Query(value = "from EmDAStock where tradeDate = ?1 order by tsCode")
    List<EmDAStock> findByTradeDate(String tradeDate);

    @Query("from EmDAStock where tsCode =?1 order by tradeDate")
    List<EmDAStock> findByCode(String code);

    @Query(value = "select distinct c.ts_code from em_d_a_stock c order by c.ts_code ", nativeQuery = true)
    List<String> findCodes();

    @Query(value = "select ts_code from em_d_a_stock where trade_date=(select max(trade_date) from em_d_a_stock) order by ts_code", nativeQuery = true)
    List<String> findLastCodes();

    @Query(value = "select * from em_d_a_stock where trade_date=:tradeDate", nativeQuery = true)
    List<EmDAStock> findCurrent(@Param("tradeDate") String tradeDate);


    @Query(value = """
            with st as (select trade_date,
                               ts_code,
                               pri_close,
                               ROW_NUMBER() over (order by trade_date desc)     as sn,
                               sum(change_hand) over (order by trade_date desc) as sh,
                               round(sum(amount) over (order by trade_date desc) / sum(vol) over (order by trade_date desc) / 100,
                                     2)                                         as sa
                        from em_d_a_stock
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

    @Query(value = "select distinct(trade_date) from em_d_a_stock order by trade_date", nativeQuery = true)
    List<String> findALlDates();

    @Query(value = "select distinct(trade_date) from em_d_a_stock where ts_code = :code order by trade_date", nativeQuery = true)
    List<String> findAllDateByCode(@Param("code") String code);


}
