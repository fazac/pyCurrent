package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.BoardIndustryCon;
import com.stock.pycurrent.entity.pk.BasicStockPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author fzc
 * @date 2024/6/25 10:33
 * @description
 */
@Repository
public interface BoardIndustryConRepo extends JpaRepository<BoardIndustryCon, BasicStockPK> {
    @Query(value = "select group_concat(symbol) from board_industry_con where ts_code = :code and  trade_date = (select max(trade_date) from board_industry_con) group by ts_code ", nativeQuery = true)
    String findByCode(@Param("code") String code);
}
