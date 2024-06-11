package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.CurConcernCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurConcernCodeRepo extends JpaRepository<CurConcernCode, String> {
    @Query("from CurConcernCode where tradeDate = :tradeDate")
    List<CurConcernCode> findLast(@Param("tradeDate") String tradeDate);

}
