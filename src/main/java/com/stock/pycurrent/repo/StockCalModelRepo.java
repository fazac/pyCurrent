package com.stock.pycurrent.repo;


import com.stock.pycurrent.entity.StockCalModel;
import com.stock.pycurrent.entity.pk.BasicStockPK;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockCalModelRepo extends JpaRepository<StockCalModel, BasicStockPK> {
//    @QueryHints(value = @QueryHint(name = "jakarta.persistence.cache.storeMode", value = "REFRESH"))
    @Query(value = "select * from stock_cal_model s where s.ts_code = :code and s.level = (select max(level) from stock_cal_model where ts_code = :code)", nativeQuery = true)
    List<StockCalModel> findLastByCode(@Param("code") String code);
}
