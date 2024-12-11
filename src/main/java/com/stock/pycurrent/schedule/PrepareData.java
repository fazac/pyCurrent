package com.stock.pycurrent.schedule;

import com.stock.pycurrent.entity.CodeLabel;
import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.service.CodeLabelService;
import com.stock.pycurrent.service.LastHandPriService;
import com.stock.pycurrent.service.StockService;
import com.stock.pycurrent.util.DateUtils;
import com.stock.pycurrent.util.PARAMS;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private CodeLabelService codeLabelService;
    @Resource
    private LastHandPriService lastHandPriService;
    private static final Map<String, List<String>> labelMap = new ConcurrentHashMap<>();
    private static final Map<String, EmRealTimeStock> emRealTimeStockMap = new ConcurrentHashMap<>();

    @Override
    public void run(String... args) {
        LocalDateTime n = LocalDateTime.now();
        log.warn("START " + DateUtils.getH_M(n));
//        pullAll();
//        prepareLabelMap();
    }

    @SneakyThrows
    @Scheduled(cron = " 0 5 9 * * ? ")
    public void prepareLabelMap() {
        log.warn("LABEL-PREPARE-ENTER");
        labelMap.clear();
        List<CodeLabel> codeLabels = codeLabelService.findLast();
        if (codeLabels != null && !codeLabels.isEmpty()) {
            codeLabels.forEach(x -> {
                List<String> tmpLabels = new ArrayList<>(Arrays.asList(x.getConcept().split(",")));
                tmpLabels.addFirst(x.getIndustry());
                tmpLabels.addFirst(x.getName());
                labelMap.put(x.getTsCode(), tmpLabels);
            });
        }
        log.warn("LABEL-PREPARE-OVER");
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
            log.warn("ROC-ENTER");
            stockService.initRocModel();
            log.warn("ROC-OVER");
            log.warn("LHP-ENTER");
            lastHandPriService.createIntradayLHP();
            log.warn("LHP-OVER");
            log.warn("PULL-ALL-OVER");
        }
    }

    public static List<String> findLabelList(String code) {
        return labelMap.getOrDefault(code, Collections.emptyList());
    }

    public static String findLabelStr(String code) {
        return String.join(",", labelMap.getOrDefault(code, Collections.emptyList()));
    }

    public static void refreshRTMap(List<EmRealTimeStock> emRealTimeStockList) {
        emRealTimeStockMap.clear();
        emRealTimeStockList.forEach(x -> {
            emRealTimeStockMap.put(x.getTsCode(), x);
        });
    }

    public static EmRealTimeStock findRTCode(String code) {
        return emRealTimeStockMap.getOrDefault(code, null);
    }

}
