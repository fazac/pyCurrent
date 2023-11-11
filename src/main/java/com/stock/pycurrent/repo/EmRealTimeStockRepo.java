package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.entity.pk.BasicStockPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmRealTimeStockRepo extends JpaRepository<EmRealTimeStock, BasicStockPK> {
    @Query(value = """
            select t.*
            from em_real_time_stock t
            where t.trade_date = (
                select (max(a.trade_date))
                from em_real_time_stock a);""", nativeQuery = true)
    List<EmRealTimeStock> findLast();
}
