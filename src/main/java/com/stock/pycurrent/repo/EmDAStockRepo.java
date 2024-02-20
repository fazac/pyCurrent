package com.stock.pycurrent.repo;


import com.stock.pycurrent.entity.EmDAStock;
import com.stock.pycurrent.entity.pk.BasicStockPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmDAStockRepo extends JpaRepository<EmDAStock, BasicStockPK> {
    @Query(value = "select max(c.trade_date) from em_d_a_stock c", nativeQuery = true)
    String findMaxTradeDate();

    @Query("from EmDAStock where tsCode =?1 ")
    List<EmDAStock> findByCode(String code);

    @Query(value = "select distinct c.ts_code from em_d_a_stock c order by c.ts_code ", nativeQuery = true)
    List<String> findCodes();

}
