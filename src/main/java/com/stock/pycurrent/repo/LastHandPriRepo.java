package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.LastHandPri;
import com.stock.pycurrent.entity.pk.BasicStockPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author fzc
 * @date 2024/7/8 9:35
 * @description
 */
@Repository
public interface LastHandPriRepo extends JpaRepository<LastHandPri, BasicStockPK> {
    @Query(value = "select max(trade_date) from last_hand_pri", nativeQuery = true)
    String findMaxDate();

    @Query(value = "select * from last_hand_pri where ts_code = :code order by trade_date desc limit 100", nativeQuery = true)
    List<LastHandPri> findLHPByCode(@Param("code") String code);

    @Query(value = "select * from last_hand_pri where ts_code = :code order by trade_date", nativeQuery = true)
    List<LastHandPri> findByCode(@Param("code") String code);

    @Query(value = "select * from last_hand_pri where trade_date = :tradeDate order by ts_code", nativeQuery = true)
    List<LastHandPri> findByTradeDate(@Param("tradeDate") String tradeDate);


}
