package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.EmConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmConstantRepo extends JpaRepository<EmConstant, String> {
}
