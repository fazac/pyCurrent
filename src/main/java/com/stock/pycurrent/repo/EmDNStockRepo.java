package com.stock.pycurrent.repo;


import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.pk.BasicStockPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmDNStockRepo extends JpaRepository<EmDNStock, BasicStockPK> {
    @Query(value = "select max(c.trade_date) from em_d_n_stock c", nativeQuery = true)
    String findMaxTradeDate();

}
