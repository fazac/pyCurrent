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

    @Query(value = "select max(trade_date) from limit_code", nativeQuery = true)
    String findMaxDate();

    @Query(value = """
            SELECT count(1) FROM holiday_date WHERE date_year= year(curdate()) and :nowDate member of (after_value)""", nativeQuery = true)
    Long checkDateHoliday(@Param("nowDate") String nowDate);


}
