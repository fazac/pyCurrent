package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.CurCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurCountRepo extends JpaRepository<CurCount, String> {
}
