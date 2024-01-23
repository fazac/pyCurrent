package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.RealBar;
import com.stock.pycurrent.entity.pk.BasicStockPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RealBarRepo extends JpaRepository<RealBar, BasicStockPK> {
    @Query(value = """
            select * from real_bar where trade_date <:tradeDate and trade_date>curdate() and ts_code= :tsCode order by trade_date desc limit 1
            """, nativeQuery = true)
    RealBar findOne(@Param("tradeDate") String tradeDate, @Param("tsCode") String tsCode);
}
