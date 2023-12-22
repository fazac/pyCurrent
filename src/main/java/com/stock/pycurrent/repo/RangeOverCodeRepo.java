package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.RangeOverCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RangeOverCodeRepo extends JpaRepository<RangeOverCode, String> {
    @Query("from RangeOverCode where tradeDate = :tradeDate")
    RangeOverCode findByDate(@Param("tradeDate") String tradeDate);
}
