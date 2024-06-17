package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.RealBar;
import com.stock.pycurrent.entity.pk.BasicStockPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RealBarRepo extends JpaRepository<RealBar, BasicStockPK> {
    @Query(value = """
            select * from real_bar where trade_date <:tradeDate and trade_date>curdate() and ts_code= :tsCode order by trade_date desc limit 1
            """, nativeQuery = true)
    RealBar findOne(@Param("tradeDate") String tradeDate, @Param("tsCode") String tsCode);

    @Query(value = "delete from real_bar where trade_date >curdate() and ts_code = :tsCode", nativeQuery = true)
    @Transactional
    @Modifying(clearAutomatically = true)
    void deleteBarByCode(@Param("tsCode") String tsCode);

    @Query(value = """
            select count(1) from real_bar where  trade_date>curdate() and ts_code= :tsCode order by trade_date desc limit 1
            """, nativeQuery = true)
    Long findBarCount(@Param("tsCode") String tsCode);

    @Query(value = "select c.trade_date,c.bar from real_bar c where c.trade_date > curdate() and c.ts_code = :tsCode", nativeQuery = true)
    List<Object> findIntradayBar(@Param("tsCode") String tsCode);
}
