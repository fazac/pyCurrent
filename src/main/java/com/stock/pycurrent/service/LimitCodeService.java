package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.LimitCode;
import com.stock.pycurrent.repo.LimitCodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fzc
 * @date 2024/1/16 11:04
 * @description
 */

@Service
public class LimitCodeService {
    private LimitCodeRepo limitCodeRepo;

    public LimitCode findByDate(String tradeDate) {
        return limitCodeRepo.findByDate(tradeDate);
    }

    public LimitCode findLastOne(String tradeDate) {
        return limitCodeRepo.findLastOne(tradeDate);
    }

    @Autowired
    public void setLimitCodeRepo(LimitCodeRepo limitCodeRepo) {
        this.limitCodeRepo = limitCodeRepo;
    }
}
