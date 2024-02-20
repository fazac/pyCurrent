package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.entity.emum.PyFuncEnum;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.repo.EmRealTimeStockRepo;
import com.stock.pycurrent.util.PythonScriptUtils;
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

    @SuppressWarnings("unused")
    public List<EmRealTimeStock> findStocksByCodeDate(String tsCode, String tradeDate) {
        return emRealTimeStockRepo.findStocksByCodeDate(tsCode, tradeDate);
    }

    public List<EmRealTimeStock> findEmCurrent() {
        return PythonScriptUtils.execThreadPY(Constants.AKSHARE_EM_REALTIME, PyFuncEnum.EM_CURRENT.toString(), this::findLast);
    }

    @Autowired
    public void setEmRealTimeStockRepo(EmRealTimeStockRepo emRealTimeStockRepo) {
        this.emRealTimeStockRepo = emRealTimeStockRepo;
    }
}
