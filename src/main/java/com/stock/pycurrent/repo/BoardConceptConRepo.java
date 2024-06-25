package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.BoardConceptCon;
import com.stock.pycurrent.entity.pk.ConceptConPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardConceptConRepo extends JpaRepository<BoardConceptCon, ConceptConPK> {

    @Query(value = "select group_concat(symbol) from board_concept_con where ts_code = :code and  trade_date = (select max(trade_date) from board_concept_con) group by ts_code ", nativeQuery = true)
    String findByCode(@Param("code") String code);
}
