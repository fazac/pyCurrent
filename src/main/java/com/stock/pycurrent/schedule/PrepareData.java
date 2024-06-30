package com.stock.pycurrent.schedule;

import com.stock.pycurrent.service.*;
import com.stock.pycurrent.util.DateUtils;
import com.stock.pycurrent.util.PARAMS;
import com.stock.pycurrent.util.StockUtils;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * @author fzc
 * @date 2024/2/19 10:35
 * @description
 */
@Configuration
@CommonsLog
public class PrepareData implements CommandLineRunner {
    @Resource
    private StockService stockService;
    @Resource
    private BoardConceptConService boardConceptConService;
    @Resource
    private BoardIndustryConService boardIndustryConService;
    @Resource
    private EmRealTimeStockService emRealTimeStockService;
    @Resource
    private ContinuousUpService continuousUpService;
    @Resource
    private LimitCodeService limitCodeService;
    @Resource
    private CodeLabelService codeLabelService;

    @Override
    public void run(String... args) {
        LocalDateTime n = LocalDateTime.now();
        log.warn("START " + DateUtils.getH_M(n));
        if (PARAMS.BAK_MODE
                && StockUtils.isNotRest()
                && !limitCodeService.checkDateHoliday(DateUtils.getM_D(n))) {
//            createTable();
//            pullAll();
            rocCal();
        }
    }

    @SneakyThrows
    @Scheduled(cron = " 0 35 16 * * ? ")
    public void pullAll() {
        if (!PARAMS.BAK_MODE) {
//            log.warn("PULL-ALL-ENTER");
//            log.warn("pullData-EM-ENTER");
//            stockService.initEMDailyData();
//            log.warn("pullData-EM-OVER");
//            if (StockUtils.afterPullHour()) {
//                log.warn("pullData-EMBC-ENTER");
//                boardConceptConService.findBoardConceptConCurrent();
//                log.warn("pullData-EMBC-OVER");
//                log.warn("pullData-EMBI-ENTER");
//                boardIndustryConService.findBoardIndustryConCurrent();
//                log.warn("pullData-EMBI-OVER");
//                log.warn("continuousUp-ENTER");
//                continuousUpService.initContinuousUp();
//                log.warn("continuousUp-OVER");
            log.warn("LABEL-ENTER");
            codeLabelService.createLabels();
            log.warn("LABEL-OVER");
//            }
            log.warn("createLimitCode-ENTER");
            stockService.createLimitCode();
            log.warn("createLimitCode-OVER");
            log.warn("PULL-ALL-OVER");
        }
    }

    @SneakyThrows
    @Scheduled(cron = " 0 10 17 * * ? ")
    public void rocCal() {
        if (!PARAMS.BAK_MODE) {
            log.warn("ROC-ENTER");
            stockService.initRocModel();
            log.warn("ROC-OVER");
        }
    }

//    @Scheduled(cron = " 0 0 9 * * ? ")
    public void createTable() {
        if (!PARAMS.BAK_MODE) {
            if (StockUtils.isNotRest()) {
                log.warn("createTable-ENTER");
                emRealTimeStockService.createTable();
                log.warn("createTable-OVER");
            }
        }
    }
}
