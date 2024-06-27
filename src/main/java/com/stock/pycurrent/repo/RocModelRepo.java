package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.RocModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

    @Query(value = """
            select *
            from (select ts_code,
                         max(case when rn = 1 then ratio end) as r1,
                         max(case when rn = 2 then ratio end) as r2
                  from (select ts_code, row_number() over (partition by ts_code order by sn desc) as rn, ratio
                        from roc_model
                        where params = (select max(params) from roc_model)
                        order by ts_code) t
                  where t.rn < 3
                  group by ts_code) t1
            where ((t1.r1 <= :r1HighLimit or :r1HighLimit is null) and
                   (t1.r1 >= :r1LowLimit or :r1LowLimit is null)
                and (t1.r2 <= :r2HighLimit or :r2HighLimit is null)
                and (t1.r2 >= :r2LowLimit or :r2LowLimit is null))
               or t1.r2 is null;
             """, nativeQuery = true)
    List<Object> findByLimit(@Param("r2LowLimit") BigDecimal r2LowLimit,
                             @Param("r2HighLimit") BigDecimal r2HighLimit,
                             @Param("r1LowLimit") BigDecimal r1LowLimit,
                             @Param("r1HighLimit") BigDecimal r1HighLimit);
}
