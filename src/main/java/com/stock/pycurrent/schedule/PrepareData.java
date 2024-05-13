package com.stock.pycurrent.schedule;

import com.stock.pycurrent.service.*;
import com.stock.pycurrent.util.PARAMS;
import com.stock.pycurrent.util.StockUtils;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @author fzc
 * @date 2024/2/19 10:35
 * @description
 */
@Configuration
@CommonsLog
public class PrepareData implements CommandLineRunner {

    private StockService stockService;

    private BoardConceptConService boardConceptConService;
    private BoardIndustryConService boardIndustryConService;

    private EmRealTimeStockService emRealTimeStockService;

    private ContinuousUpService continuousUpService;

    @Override
    public void run(String... args) {
        log.warn("START V2" + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute());
//        if (StockUtils.isNotRest()) {
//            createTable();
//        }
//        if (LocalDateTime.now().getDayOfWeek() != DayOfWeek.MONDAY) {
//            pullAll();
//            rocCal();
//        }
    }

    @SneakyThrows
//    @Scheduled(cron = " 0 30 16 * * ? ")
    @SuppressWarnings("unused")
    public void pullAll() {
        if (!PARAMS.BAK_MODE) {
            log.warn("PULL-ALL-ENTER");
            log.warn("pullData-EM-ENTER");
            stockService.initEMDailyData();
            log.warn("pullData-EM-OVER");
            if (StockUtils.afterPullHour()) {
                log.warn("pullData-EMBC-ENTER");
                boardConceptConService.findBoardConceptConCurrent();
                log.warn("pullData-EMBC-OVER");
                log.warn("pullData-EMBI-ENTER");
                boardIndustryConService.findBoardIndustryConCurrent();
                log.warn("pullData-EMBI-OVER");
                log.warn("continuousUp-ENTER");
                continuousUpService.initContinuousUp();
                log.warn("continuousUp-OVER");
            }
            log.warn("createLimitCode-ENTER");
            stockService.createLimitCode();
            log.warn("createLimitCode-OVER");
            log.warn("PULL-ALL-OVER");
        }
    }

    @SneakyThrows
//    @Scheduled(cron = " 0 10 17 * * ? ")
    @SuppressWarnings("unused")
    public void rocCal() {
        log.warn("ROC-ENTER");
        stockService.initRocModel();
        log.warn("ROC-OVER");
    }

    //    @Scheduled(cron = " 0 0 9 * * ? ")
    @SuppressWarnings("unused")
    public void createTable() {
        if (StockUtils.isNotRest()) {
            log.warn("createTable-ENTER");
            emRealTimeStockService.createTable();
            log.warn("createTable-OVER");
        }
    }

    @Autowired
    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    @Autowired
    public void setBoardConceptConService(BoardConceptConService boardConceptConService) {
        this.boardConceptConService = boardConceptConService;
    }

    @Autowired
    public void setBoardIndustryConService(BoardIndustryConService boardIndustryConService) {
        this.boardIndustryConService = boardIndustryConService;
    }

    @Autowired
    public void setEmRealTimeStockService(EmRealTimeStockService emRealTimeStockService) {
        this.emRealTimeStockService = emRealTimeStockService;
    }

    @Autowired
    public void setContinuousUpService(ContinuousUpService continuousUpService) {
        this.continuousUpService = continuousUpService;
    }
}
