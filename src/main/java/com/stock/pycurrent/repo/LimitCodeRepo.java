package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.LimitCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LimitCodeRepo extends JpaRepository<LimitCode, String> {
    @Query("from LimitCode where tradeDate = :tradeDate")
    LimitCode findByDate(@Param("tradeDate") String tradeDate);

    @Query("from LimitCode where tradeDate < :tradeDate order by tradeDate desc limit 1")
    LimitCode findLastOne(@Param("tradeDate") String tradeDate);
}
