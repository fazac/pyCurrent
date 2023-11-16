package com.stock.pycurrent.schedule;

import com.stock.pycurrent.entity.EmConstant;
import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.service.EmConstantService;
import com.stock.pycurrent.service.EmRealTimeStockService;
import com.stock.pycurrent.util.MessageUtil;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@CommonsLog
public class PullData {
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal PCH_LIMIT = BigDecimal.valueOf(18);

    private EmRealTimeStockService emRealTimeStockService;
    private EmConstantService emConstantService;

    public Map<String, Integer> codeCountMap = new HashMap<>();
    public Map<String, BigDecimal> codeMaxMap = new HashMap<>();

    @Scheduled(cron = "28/30 * 9-16 * * ?")
    public void pullRealTimeData() {
        if (isTradeHour()) {
            List<EmConstant> emConstants = emConstantService.findAll();
            String noConcerns = "";
            String concerns = "";
            if (!emConstants.isEmpty()) {
                Map<String, String> emConstantMap = emConstants.stream()
                        .filter(x -> x.getCValue() != null && !x.getCValue().isEmpty())
                        .collect(Collectors.toMap(EmConstant::getCKey, EmConstant::getCValue));
                if (emConstantMap.containsKey("NO_CONCERN_CODES")) {
                    noConcerns = emConstantMap.get("NO_CONCERN_CODES");
                }
                if (emConstantMap.containsKey("CONCERN_CODES")) {
                    concerns = emConstantMap.get("CONCERN_CODES");
                }
            }
            LocalDateTime n = LocalDateTime.now();
            String nowClock = String.format("%02d", n.getHour()) + ":" + String.format("%02d", n.getMinute());
            int index = 1;
            List<EmRealTimeStock> stockList = emRealTimeStockService.findEmCurrent();
            for (EmRealTimeStock rt : stockList) {
                boolean concerned = concerns.contains(rt.getTsCode());
                boolean noConcerned = noConcerns.contains(rt.getTsCode());
                if ((!rt.getName().contains("ST") && !rt.getName().contains("退")
                        && rt.getTsCode().startsWith("3") && !noConcerned
                        && rt.getPctChg() != null && rt.getPctChg().compareTo(BigDecimal.ZERO) > 0
                        && rt.getPriHigh() != null
                        && calRatio(rt.getPriHigh(), rt.getPriClosePre()).compareTo(PCH_LIMIT) > 0
//                        && rt.getChangeHand().compareTo(HAND_LIMIT) < 0
                ) || concerned) {
                    String remarks = "-" + index++ + (concerned ? "(C)" : "(F)");
                    log.info(nowClock + " " + remarks + ": " + rt.getTsCode().substring(2, 6)
                            + " h= " + rt.getChangeHand()
                            + " rt= " + rt.getPctChg()
                    );
                    if (!concerned) {
                        if (codeCountMap.containsKey(rt.getTsCode())) {
                            if (rt.getPctChg().compareTo(codeMaxMap.get(rt.getTsCode())) == 0) {
                                codeCountMap.put(rt.getTsCode(), codeCountMap.get(rt.getTsCode()) + 1);
                            } else {
                                if (codeMaxMap.get(rt.getTsCode()).compareTo(rt.getPctChg()) < 0) {
                                    codeCountMap.put(rt.getTsCode(), 0);
                                }
                                codeMaxMap.put(rt.getTsCode(), codeMaxMap.get(rt.getTsCode()).max(rt.getPctChg()));
                            }
                            if (codeCountMap.get(rt.getTsCode()) > 3 && rt.getPctChg().compareTo(codeMaxMap.get(rt.getTsCode())) < 0) {
                                MessageUtil.sendMessage("deal one " + rt.getTsCode().substring(2, 6));
                            }
                        } else {
                            codeCountMap.put(rt.getTsCode(), 0);
                            codeMaxMap.put(rt.getTsCode(), rt.getPctChg());
                            MessageUtil.sendMessage("new one " + rt.getTsCode().substring(2, 6));
                        }
                    }
                }
            }
            log.info("--------");
        }
    }

    private boolean isTradeHour() {
        LocalDateTime n = LocalDateTime.now();
        String res = String.format("%02d", n.getHour()) + String.format("%02d", n.getMinute());
        int tmp = Integer.parseInt(res);
        return tmp >= 914 && tmp < 1130 || tmp >= 1259 && tmp <= 1510;
    }

    private BigDecimal calRatio(BigDecimal curClosePri, BigDecimal doorPri) {
        return curClosePri.subtract(doorPri).multiply(HUNDRED).divide(doorPri, 2, RoundingMode.HALF_UP);
    }

    @Autowired
    public void setEmRealTimeStockService(EmRealTimeStockService emRealTimeStockService) {
        this.emRealTimeStockService = emRealTimeStockService;
    }

    @Autowired
    public void setEmConstantService(EmConstantService emConstantService) {
        this.emConstantService = emConstantService;
    }
}
