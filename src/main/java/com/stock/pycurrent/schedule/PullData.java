package com.stock.pycurrent.schedule;

import com.stock.pycurrent.entity.EmConstant;
import com.stock.pycurrent.entity.EmConstantValue;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@CommonsLog
public class PullData {
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal PCH_LIMIT = BigDecimal.valueOf(18);
    private static final BigDecimal PCH_OVER_LIMIT = BigDecimal.valueOf(17);
    private static final BigDecimal RANGE_LIMIT = BigDecimal.valueOf(6);
    @SuppressWarnings("unused")
    private static final BigDecimal nOne = BigDecimal.ONE.negate();

    private EmRealTimeStockService emRealTimeStockService;
    private EmConstantService emConstantService;

    public Map<String, Integer> codeCountMap = new HashMap<>();

    public Map<String, Integer> codeOverLimitMap = new HashMap<>();
    public Map<String, BigDecimal> codeMaxMap = new HashMap<>();

    public Set<String> rangeOverLimitCodes = new HashSet<>();

    @Scheduled(cron = "28/30 * 9-16 * * ?")
    public void pullRealTimeData() {
        if (isTradeHour()) {
            List<EmConstant> emConstants = emConstantService.findAll();
            String noConcerns = "";
            String concerns = "";
            String holdCodes = "";
            Map<String, EmConstantValue> constantValueMap = new HashMap<>();
            Map<String, Queue<BigDecimal>> codePctMap = new HashMap<>();

            if (!emConstants.isEmpty()) {
                Map<String, EmConstant> emConstantMap = emConstants.stream()
                        .filter(x -> x.getCValue() != null && !x.getCValue().isEmpty() && !x.getCValue().trim().isEmpty())
                        .collect(Collectors.toMap(EmConstant::getCKey, Function.identity()));
                if (emConstantMap.containsKey("NO_CONCERN_CODES")) {
                    noConcerns = emConstantMap.get("NO_CONCERN_CODES").getCValue();
                }
                if (emConstantMap.containsKey("CONCERN_CODES")) {
                    concerns = emConstantMap.get("CONCERN_CODES").getCValue();
                }
                if (emConstantMap.containsKey("HOLD_CODES")) {
                    holdCodes = emConstantMap.get("HOLD_CODES").getCValue();
                    if (emConstantMap.get("HOLD_CODES").getMultiValue() != null && !emConstantMap.get("HOLD_CODES").getMultiValue().isEmpty()) {
                        constantValueMap = emConstantMap.get("HOLD_CODES").getMultiValue().stream().collect(Collectors.toMap(EmConstantValue::getTsCode, Function.identity()));
                    }
                }
            }
            String nowClock;
            int index = 1;
//            List<EmRealTimeStock> allList = emRealTimeStockService.findAll();
//            Map<String, List<EmRealTimeStock>> tmpMap = allList.stream().collect(Collectors.groupingBy(EmRealTimeStock::getTradeDate));
//            List<String> keys = new ArrayList<>(tmpMap.keySet());
//            Collections.sort(keys);
//            for (String s : keys) {
//                List<EmRealTimeStock> stockList = tmpMap.get(s);
            List<EmRealTimeStock> stockList = emRealTimeStockService.findEmCurrent();
            for (EmRealTimeStock rt : stockList) {
                boolean concerned = concerns.contains(rt.getTsCode());
                boolean noConcerned = noConcerns.contains(rt.getTsCode());
                boolean holds = holdCodes.contains(rt.getTsCode());
                boolean rangeOverLimit;
                if (!rt.getName().contains("ST") && !rt.getName().contains("退")
                        && rt.getTsCode().startsWith("3")
                        && !noConcerned
                        && rt.getPctChg() != null) {
                    if (codePctMap.containsKey(rt.getTsCode())) {
                        codePctMap.get(rt.getTsCode()).add(rt.getPctChg());
                    } else {
                        Queue<BigDecimal> pchQueue = new LinkedList<>();
                        pchQueue.add(rt.getPctChg());
                        codePctMap.put(rt.getTsCode(), pchQueue);
                    }
                    Queue<BigDecimal> fiveMinutesPch = codePctMap.get(rt.getTsCode());
                    if (fiveMinutesPch.size() > 10) {
                        fiveMinutesPch.poll();
                    }
                    rangeOverLimit = calRange(fiveMinutesPch);
                    if (rangeOverLimit) {
                        rangeOverLimitCodes.add(rt.getTsCode());
                    }
                }
                rangeOverLimit = rangeOverLimitCodes.contains(rt.getTsCode());
                if ((!rt.getName().contains("ST") && !rt.getName().contains("退")
                        && rt.getTsCode().startsWith("3") && !noConcerned
                        && rt.getPctChg() != null && rt.getPctChg().compareTo(BigDecimal.ZERO) > 0
                        && rt.getPriHigh() != null
                        && calRatio(rt.getPriHigh(), rt.getPriClosePre()).compareTo(PCH_LIMIT) > 0
//                        && rt.getChangeHand().compareTo(HAND_LIMIT) < 0
                ) || concerned || holds || rangeOverLimit) {
                    nowClock = rt.getTradeDate().substring(11);
                    String remarks = "-" + index++ + (concerned ? "(C)" : holds ? "(H)" : rangeOverLimit ? "(R)" : "(F)");
                    String holdRemark = (holds && rt.getCurrentPri() != null && constantValueMap.containsKey(rt.getTsCode()) && constantValueMap.get(rt.getTsCode()).getPrice() != null
                            ? " rr= " + (calRatio(rt.getCurrentPri(), constantValueMap.get(rt.getTsCode()).getPrice()))
                            : "");
                    log.info(nowClock + " " + remarks + ": " + rt.getTsCode().substring(2, 6)
                            + " h= " + rt.getChangeHand()
                            + " rt= " + rt.getPctChg()
                            + holdRemark
                    );
                    if (holds && rt.getPriOpen() != null && rt.getPriHigh() != null
                            && constantValueMap.containsKey(rt.getTsCode()) && constantValueMap.get(rt.getTsCode()).isSellable()) {
                        //低开超1%,涨超买入价回落卖出
                        if (calRatio(rt.getPriOpen(), rt.getPriClosePre()).compareTo(nOne) < 0
                                && rt.getPriHigh().compareTo(rt.getPriClosePre()) >= 0
                                && rt.getPctChg().compareTo(BigDecimal.ZERO) < 0
                        ) {
                            MessageUtil.sendNotificationMsg("SELL ONE ", rt.getTsCode().substring(2, 6));
                        }
                        //高开超1%且跌落买入价时卖出,或者非封板收盘卖
                        if (calRatio(rt.getPriOpen(), rt.getPriClosePre()).compareTo(BigDecimal.ONE) > 0
                                && rt.getPctChg().compareTo(BigDecimal.ZERO) < 0
                        ) {
                            MessageUtil.sendNotificationMsg("SELL ONE ", rt.getTsCode().substring(2, 6));
                        }
                    }
                    if (!concerned && !holds) {
                        if (codeCountMap.containsKey(rt.getTsCode())) {
                            if (rt.getPctChg().compareTo(codeMaxMap.get(rt.getTsCode())) == 0) {
                                // max count++;
                                codeCountMap.put(rt.getTsCode(), codeCountMap.get(rt.getTsCode()) + 1);
                            } else {
                                if (codeMaxMap.get(rt.getTsCode()).compareTo(rt.getPctChg()) < 0) {
                                    //reset count when meet greater one;
                                    codeCountMap.put(rt.getTsCode(), 0);
                                }
                                //put greater one
                                codeMaxMap.put(rt.getTsCode(), codeMaxMap.get(rt.getTsCode()).max(rt.getPctChg()));
                            }
                            if (codeCountMap.get(rt.getTsCode()) > 3
                                    && rt.getPctChg().compareTo(codeMaxMap.get(rt.getTsCode())) < 0) { // 触发涨停板
                                // info && reset count
                                MessageUtil.sendNotificationMsg("BUY ONE ", rt.getTsCode().substring(2, 6));
                                codeCountMap.put(rt.getTsCode(), 0);
                            }
                        } else {
                            // info new
                            codeCountMap.put(rt.getTsCode(), 0);
                            codeMaxMap.put(rt.getTsCode(), rt.getPctChg());
                            MessageUtil.sendNotificationMsg("NEW ONE ", rt.getTsCode().substring(2, 6));
                        }
                        if (rt.getPctChg().compareTo(PCH_OVER_LIMIT) > 0) { // 17
                            codeOverLimitMap.put(rt.getTsCode(), codeOverLimitMap.getOrDefault(rt.getTsCode(), 0) + 1);
                        }
                        if (codeOverLimitMap.getOrDefault(rt.getTsCode(), 0) > 7
                                && rt.getPctChg().compareTo(codeMaxMap.get(rt.getTsCode())) < 0) {
                            MessageUtil.sendNotificationMsg("BUY LONE ", rt.getTsCode().substring(2, 6));
                            codeOverLimitMap.put(rt.getTsCode(), 0);
                        }
                    }
                }
            }
            log.info("--------");
//            }
        }
    }

    private boolean calRange(Queue<BigDecimal> values) {
        return Collections.max(values).subtract(Collections.min(values)).compareTo(RANGE_LIMIT) >= 0;
    }

    private boolean isTradeHour() {
        LocalDateTime n = LocalDateTime.now();
        String res = String.format("%02d", n.getHour()) + String.format("%02d", n.getMinute());
        int tmp = Integer.parseInt(res);
        return tmp >= 914 && tmp < 1131 || tmp >= 1259 && tmp <= 1510;
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
