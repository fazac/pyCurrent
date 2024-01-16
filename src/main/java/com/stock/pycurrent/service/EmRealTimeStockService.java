package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.repo.EmRealTimeStockRepo;
import com.stock.pycurrent.util.ExecutorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmRealTimeStockService {
    private EmRealTimeStockRepo emRealTimeStockRepo;

    public List<EmRealTimeStock> findLast() {
        return emRealTimeStockRepo.findLast();
    }

    public List<String> findTradeDates() {
        return emRealTimeStockRepo.findTradeDates();
    }

    public List<EmRealTimeStock> findStockByDate(String tradeDate) {
        return emRealTimeStockRepo.findStockByDate(tradeDate);
    }

    public List<EmRealTimeStock> findStocksByCodeDate(String tsCode, String tradeDate) {
        return emRealTimeStockRepo.findStocksByCodeDate(tsCode, tradeDate);
    }


    public List<EmRealTimeStock> findEmCurrent() {
        return ExecutorUtils.execThreadPY(this::findLast);
    }

    @Autowired
    public void setEmRealTimeStockRepo(EmRealTimeStockRepo emRealTimeStockRepo) {
        this.emRealTimeStockRepo = emRealTimeStockRepo;
    }
}
