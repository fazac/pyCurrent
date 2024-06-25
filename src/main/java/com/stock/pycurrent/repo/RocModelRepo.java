package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.RocModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RocModelRepo extends JpaRepository<RocModel, Integer> {

    @Query(value = "select EXISTS(select * from roc_model where params = :params)", nativeQuery = true)
    long checkExistParam(@Param("params") String params);

    @Query(value = """
                    select *
                        from roc_model
                        where params=(select max(params) from roc_model) and ts_code =:code order by ts_code ;
            """, nativeQuery = true)
    List<RocModel> findByCode(@Param("code") String code);
}
