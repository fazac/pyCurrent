package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.EmConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmConstantRepo extends JpaRepository<EmConstant, String> {
    @Query(value = "from EmConstant where cKey = :key ")
    EmConstant findByKey(@Param("key") String key);
}
