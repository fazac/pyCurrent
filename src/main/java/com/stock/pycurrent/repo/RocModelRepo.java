package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.RocModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RocModelRepo extends JpaRepository<RocModel, Integer> {

    @Query(value = "select EXISTS(select * from roc_model where params = :params)", nativeQuery = true)
    long checkExistParam(@Param("params") String params);

}
