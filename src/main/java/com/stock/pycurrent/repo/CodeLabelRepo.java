package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.CodeLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeLabelRepo extends JpaRepository<CodeLabel, String> {
    @Query(value = " select * from code_label where trade_date = (select max(trade_date) from code_label)", nativeQuery = true)
    List<CodeLabel> findLast();

    @Query(value = "select * from code_label where ts_code = :code and trade_date = (select max(trade_date) from code_label)", nativeQuery = true)
    CodeLabel findByCode(@Param("code") String code);
}
