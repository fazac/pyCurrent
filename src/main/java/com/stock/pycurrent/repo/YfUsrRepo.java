package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.YfUsr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YfUsrRepo extends JpaRepository<YfUsr, String> {
}
