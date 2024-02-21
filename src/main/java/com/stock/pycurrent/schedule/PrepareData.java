package com.stock.pycurrent.schedule;

import com.stock.pycurrent.service.BoardConceptConService;
import com.stock.pycurrent.service.BoardIndustryConService;
import com.stock.pycurrent.service.EmRealTimeStockService;
import com.stock.pycurrent.service.StockService;
import com.stock.pycurrent.util.PARAMS;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

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

    @Override
    public void run(String... args) {
        createTable();
        pullAll();
        rocCal();
    }

    @SneakyThrows
    @Scheduled(cron = " 0 15 16 * * ? ")
    public void pullAll() {
        if (!PARAMS.BAK_MODE) {
            log.warn("PULL-ALL-ENTER");
            log.warn("pullData-EM-ENTER");
            stockService.initEMDailyData();
            log.warn("pullData-EM-OVER");
            log.warn("pullData-EMBC-ENTER");
            boardConceptConService.findBoardConceptConCurrent();
            log.warn("pullData-EMBC-OVER");
            log.warn("pullData-EMBI-ENTER");
            boardIndustryConService.findBoardIndustryConCurrent();
            log.warn("pullData-EMBI-OVER");
            log.warn("PULL-ALL-OVER");
            log.warn("createLimitCode-ENTER");
            stockService.createLimitCode();
            log.warn("createLimitCode-OVER");
        }
    }

    @SneakyThrows
    @Scheduled(cron = " 0 0 17 * * ? ")
    public void rocCal() {
        log.warn("ROC-ENTER");
        stockService.initRocModel();
        log.warn("ROC-OVER");
    }

    @Scheduled(cron = " 0 0 9 * * ? ")
    public void createTable() {
        log.warn("createTable-ENTER");
        emRealTimeStockService.createTable();
        log.warn("createTable-OVER");
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
}
