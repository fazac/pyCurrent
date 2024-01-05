package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.RealBar;
import com.stock.pycurrent.repo.RealBarRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fzc
 * @date 2024/1/5 14:54
 * @description
 */
@Service
public class RealBarService {
    private RealBarRepo realBarRepo;

    public RealBar findOne(String tradeDate, String tsCode) {
        return realBarRepo.findOne(tradeDate, tsCode);
    }

    public RealBar save(RealBar realBar) {
        return realBarRepo.saveAndFlush(realBar);
    }

    @Autowired
    public void setRealBarRepo(RealBarRepo realBarRepo) {
        this.realBarRepo = realBarRepo;
    }
}
