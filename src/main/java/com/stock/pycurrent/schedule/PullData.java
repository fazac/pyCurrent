package com.stock.pycurrent.schedule;

import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.service.EmRealTimeStockService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@CommonsLog
public class PullData {

    private EmRealTimeStockService emRealTimeStockService;

    @Scheduled(cron = "27/30 * 9-16 * * ?")
    public void pullRealTimeData() {
        if (isTradeHour()) {
            List<EmRealTimeStock> stockList = emRealTimeStockService.findEmCurrent();
            log.info("size : " + stockList.size());
        }
    }

    private boolean isTradeHour() {
        LocalDateTime n = LocalDateTime.now();
        String res = String.format("%02d", n.getHour()) + String.format("%02d", n.getMinute());
        int tmp = Integer.parseInt(res);
        return tmp >= 914 && tmp < 1130 || tmp >= 1259 && tmp <= 1510;
    }

    @Autowired
    public void setEmRealTimeStockService(EmRealTimeStockService emRealTimeStockService) {
        this.emRealTimeStockService = emRealTimeStockService;
    }
}
