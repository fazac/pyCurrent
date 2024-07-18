package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.CurCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurCountRepo extends JpaRepository<CurCount, String> {
    @Query(value = "select * from cur_count where trade_date > (select left(max(trade_date),10) from cur_count) order by trade_date desc", nativeQuery = true)
    List<CurCount> findLastAll();

    @Query(value = "select * from cur_count where  is_summary = '1' order by trade_date desc", nativeQuery = true)
    List<CurCount> findSummaryList();

    @Query(value = """
            select *
            from cur_count
            where trade_date > (select left(max(trade_date), 10)
                                from cur_count
                                where trade_date < (select left(max(trade_date), 10) from cur_count))
              and trade_date < (select left(max(trade_date), 10) from cur_count);
            """, nativeQuery = true)
    List<CurCount> findYesterdayAll();
}
