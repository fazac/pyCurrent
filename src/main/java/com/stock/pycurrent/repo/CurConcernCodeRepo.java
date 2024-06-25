package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.CurConcernCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurConcernCodeRepo extends JpaRepository<CurConcernCode, String> {
    @Query(value = " select * from cur_concern_code where trade_date = (select max(trade_date) from cur_concern_code) and tabel_show = '1' ", nativeQuery = true)
    List<CurConcernCode> findLast();

}
