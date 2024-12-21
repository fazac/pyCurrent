package com.stock.pycurrent.repo;


import com.stock.pycurrent.entity.StockCalModel;
import com.stock.pycurrent.entity.pk.StockCalModelPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockCalModelRepo extends JpaRepository<StockCalModel, StockCalModelPK> {
    //    @QueryHints(value = @QueryHint(name = "jakarta.persistence.cache.storeMode", value = "REFRESH"))
    @Query(value = "select * from stock_cal_model s where s.ts_code = :code and s.level = (select max(level) from stock_cal_model where ts_code = :code and type=:type) and s.type=:type order by trade_date", nativeQuery = true)
    List<StockCalModel> findLastByCode(@Param("code") String code, @Param("type") Integer type);

    @Query(value = "select * from stock_cal_model where ts_code = :code and type = :type and trade_date>=:startDate and trade_date<=:endDate order by trade_date", nativeQuery = true)
    List<StockCalModel> findStockCalModels(@Param("code") String code,
                                           @Param("type") int type,
                                           @Param("startDate") String startDate,
                                           @Param("endDate") String endDate);

    @Query(value = "select * from stock_cal_model where ts_code = :code and type = :type order by trade_date", nativeQuery = true)
    List<StockCalModel> findStockCalModels(@Param("code") String code,
                                           @Param("type") int type);

    @Query(value = "select max(trade_date) from stock_cal_model where type = :type",nativeQuery = true)
    String findMaxDate(@Param("type")int type);
}
