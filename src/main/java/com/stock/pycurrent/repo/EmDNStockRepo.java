package com.stock.pycurrent.repo;


import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.pk.BasicStockPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
}
