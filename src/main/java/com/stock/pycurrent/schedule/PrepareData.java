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
    private LimitCodeService limitCodeService;
    @Resource
    private CodeLabelService codeLabelService;

    @Override
    public void run(String... args) {
        LocalDateTime n = LocalDateTime.now();
        log.warn("START " + DateUtils.getH_M(n));
        pullAll();
    }

    @SneakyThrows
    @Scheduled(cron = " 0 35 16 * * ? ")
    public void pullAll() {
        if (!PARAMS.BAK_MODE) {
            log.warn("LABEL-ENTER");
            codeLabelService.createLabels();
            log.warn("LABEL-OVER");
            log.warn("createLimitCode-ENTER");
            stockService.createLimitCode();
            log.warn("createLimitCode-OVER");
            log.warn("PULL-ALL-OVER");
            log.warn("ROC-ENTER");
            stockService.initRocModel();
            log.warn("ROC-OVER");
        }
    }

}
