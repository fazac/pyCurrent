package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.RocModel;
import com.stock.pycurrent.entity.StockCalModel;
import com.stock.pycurrent.repo.StockCalModelRepo;
import com.stock.pycurrent.util.StockUtils;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@CommonsLog
public class StockCalModelService {
    @Resource
    private StockCalModelRepo stockCalModelRepo;

    public List<RocModel> findStockCalModels(String code,
                                             int type,
                                             String startDate,
                                             String endDate) {
        List<StockCalModel> stockCalModels;
        if (startDate != null && !startDate.trim().isEmpty()) {
            stockCalModels = stockCalModelRepo.findStockCalModels(code, type, startDate, endDate);
        } else {
            stockCalModels = stockCalModelRepo.findStockCalModels(code, type);
        }
        if (stockCalModels != null && !stockCalModels.isEmpty()) {
            return StockUtils.convertRocModel(stockCalModels, new Date());
        }
        return Collections.emptyList();
    }


}
