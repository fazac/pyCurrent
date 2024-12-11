package com.stock.pycurrent.repo;

import com.stock.pycurrent.entity.YfToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface YfTokenRepo extends JpaRepository<YfToken, String> {
    @Query(value = "select public_key from yf_token where token=:token and expire_date > curdate()", nativeQuery = true)
    String findPubKeyByToken(@Param("token") String token);
}
