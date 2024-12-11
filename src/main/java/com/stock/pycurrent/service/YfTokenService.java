package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.YfToken;
import com.stock.pycurrent.repo.YfTokenRepo;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class YfTokenService {
    @Resource
    private YfTokenRepo yfTokenRepo;

    public void addToken(YfToken yfToken) {
        yfTokenRepo.save(yfToken);
    }


    public String findPublicKeyByToken(String token) {
        return yfTokenRepo.findPubKeyByToken(token);
    }

}
