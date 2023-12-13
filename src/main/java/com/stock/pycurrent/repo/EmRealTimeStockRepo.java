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
            where t.trade_date = (select (max(a.trade_date)) from em_real_time_stock a)
            order by t.ts_code;""", nativeQuery = true)
    List<EmRealTimeStock> findLast();
@Query(value = """
        select *
        from em_real_time_stock
        where trade_date >= '2023-12-13 09:30:00'
          and trade_date < '2023-12-13 10:00:00';
        """,nativeQuery = true)
    List<EmRealTimeStock> findSps();
}
