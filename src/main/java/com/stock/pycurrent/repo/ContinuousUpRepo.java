package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.ContinuousUp;
import com.stock.pycurrent.entity.pk.ContinuousUpPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContinuousUpRepo extends JpaRepository<ContinuousUp, ContinuousUpPK> {
    @Query(value = """
                select *
                from continuous_up
                where continuous_up.statics_date = (select max(statics_date) from continuous_up);
            """,nativeQuery = true)
    List<ContinuousUp> findLast();
}
