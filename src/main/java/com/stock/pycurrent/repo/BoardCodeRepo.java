package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.BoardCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardCodeRepo extends JpaRepository<BoardCode, String> {
    @Query("from BoardCode where tradeDate = :tradeDate")
    BoardCode findByDate(@Param("tradeDate") String tradeDate);

}
