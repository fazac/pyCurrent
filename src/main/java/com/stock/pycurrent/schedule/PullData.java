package com.stock.pycurrent.schedule;

import com.stock.pycurrent.entity.*;
import com.stock.pycurrent.entity.jsonvalue.EmConstantValue;
import com.stock.pycurrent.entity.jsonvalue.RangeOverCodeValue;
import com.stock.pycurrent.service.EmConstantService;
import com.stock.pycurrent.service.EmRealTimeStockService;
import com.stock.pycurrent.service.RangeOverCodeService;
import com.stock.pycurrent.service.RealBarService;
import com.stock.pycurrent.util.CalculateUtils;
import com.stock.pycurrent.util.MessageUtil;
import com.stock.pycurrent.util.PARAMS;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@CommonsLog
public class PullData implements CommandLineRunner {
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal PCH_LIMIT = BigDecimal.valueOf(18);
    private static final BigDecimal PCH_OVER_LIMIT = BigDecimal.valueOf(17);
    private static final BigDecimal RANGE_LIMIT = BigDecimal.valueOf(5);
    private static final BigDecimal MAX_LIMIT = BigDecimal.valueOf(21);
    private static final BigDecimal nOne = BigDecimal.ONE.negate();
    private static final BigDecimal HUNDRED_MILLION = BigDecimal.valueOf(100000000);

    private static final int SHORT_PERIOD = 12;
    private static final int LONG_PERIOD = 26;
    private static final int MID_PERIOD = 9;
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final String NOW_DAY = LocalDateTime.now().format(DATE_TIME_FORMAT);

    private final String CODE_TYPE = "F,R,C,L,H";

    private EmRealTimeStockService emRealTimeStockService;
    private EmConstantService emConstantService;
    private RangeOverCodeService rangeOverCodeService;

    private RealBarService realBarService;

    private final Map<String, Integer> codeCountMap = new HashMap<>();

    private final Map<String, Integer> codeOverLimitMap = new HashMap<>();
    private final Map<String, BigDecimal> codeMaxMap = new HashMap<>();

    private final Map<String, List<BigDecimal>> codePctMap = new HashMap<>();

    private final Map<String, List<String>> logsMap = new HashMap<>();

    private final Map<String, Long> volMap = new HashMap<>();
    private final Map<String, Long> volStepMap = new HashMap<>();
    private final Map<String, BigDecimal> amountMap = new HashMap<>();


    private void initLogsMap() {
        logsMap.clear();
        Arrays.stream(CODE_TYPE.split(",")).forEach(x -> logsMap.put(x, new ArrayList<>()));
    }

    @Override
    public void run(String... args) {
        if (PARAMS.BAK_MODE) {
            pullTest();
        }
    }

    @Scheduled(cron = "28/30 * 9-16 * * ?")
    public void pullRealTimeData() {
        if (isTradeHour()) {
            List<EmConstant> emConstants = emConstantService.findAll();
            String[] codes = prepareConstantsCodes(emConstants);
            Map<String, Map<String, EmConstantValue>> stockMap = prepareConstantsMap(emConstants);

            List<EmRealTimeStock> stockList = emRealTimeStockService.findEmCurrent();
            checkRealData(codes, stockMap, codePctMap, logsMap, stockList);
        }
    }


    @SneakyThrows
    public void pullTest() {
        List<EmConstant> emConstants = emConstantService.findAll();
        String[] codes = prepareConstantsCodes(emConstants);
        Map<String, Map<String, EmConstantValue>> stockMap = prepareConstantsMap(emConstants);
        List<String> times = emRealTimeStockService.findTradeDates();
        for (String s : times) {
            List<EmRealTimeStock> stockList = emRealTimeStockService.findStockByDate(s);
            checkRealData(codes, stockMap, codePctMap, logsMap, stockList);
        }
    }


    private String[] prepareConstantsCodes(List<EmConstant> emConstants) {
        String concernCodes = "";
        String noConcernCodes = "";
        String holdCodes = "";
        String noBuyCodes = "";
        String yesterdayCodes = "";
        if (!emConstants.isEmpty()) {
            Map<String, EmConstant> emConstantMap = emConstants.stream().collect(Collectors.toMap(EmConstant::getCKey, Function.identity()));
            concernCodes = emConstantMap.containsKey("CONCERN_CODES") ? emConstantMap.get("CONCERN_CODES").getCValue() : "";
            concernCodes = concernCodes == null || concernCodes.isEmpty() ? "" : concernCodes;
            noConcernCodes = emConstantMap.containsKey("NO_CONCERN_CODES") ? emConstantMap.get("NO_CONCERN_CODES").getCValue() : "";
            noConcernCodes = noConcernCodes == null || noConcernCodes.isEmpty() ? "" : noConcernCodes;
            holdCodes = emConstantMap.containsKey("HOLD_CODES") ? emConstantMap.get("HOLD_CODES").getCValue() : "";
            holdCodes = holdCodes == null || holdCodes.isEmpty() ? "" : holdCodes;
            noBuyCodes = emConstantMap.containsKey("NO_BUY_CODES") ? emConstantMap.get("NO_BUY_CODES").getCValue() : "";
            noBuyCodes = noBuyCodes == null || noBuyCodes.isEmpty() ? "" : noBuyCodes;
            yesterdayCodes = emConstantMap.containsKey("YESTERDAY_CODES") ? emConstantMap.get("YESTERDAY_CODES").getCValue() : "";
            yesterdayCodes = yesterdayCodes == null || yesterdayCodes.isEmpty() ? "" : yesterdayCodes;
        }
        return new String[]{concernCodes, noConcernCodes, holdCodes, noBuyCodes, yesterdayCodes};
    }

    private Map<String, Map<String, EmConstantValue>> prepareConstantsMap(List<EmConstant> emConstants) {
        Map<String, Map<String, EmConstantValue>> constantValueMap = new HashMap<>();
        if (!emConstants.isEmpty()) {
            Map<String, EmConstant> emConstantMap = emConstants.stream().collect(Collectors.toMap(EmConstant::getCKey, Function.identity()));
            if (emConstantMap.containsKey("CONCERN_CODES") && emConstantMap.get("CONCERN_CODES").getMultiValue() != null && !emConstantMap.get("CONCERN_CODES").getMultiValue().isEmpty()) {
                Map<String, EmConstantValue> tmpMap = emConstantMap.get("CONCERN_CODES").getMultiValue().stream().collect(Collectors.toMap(EmConstantValue::getTsCode, Function.identity()));
                constantValueMap.put("CONCERN_CODES", tmpMap);
            }
            if (emConstantMap.containsKey("HOLD_CODES") && emConstantMap.get("HOLD_CODES").getMultiValue() != null && !emConstantMap.get("HOLD_CODES").getMultiValue().isEmpty()) {
                Map<String, EmConstantValue> tmpMap = emConstantMap.get("HOLD_CODES").getMultiValue().stream().filter(x -> x.getTsCode() != null && !x.getTsCode().isBlank()).collect(Collectors.toMap(EmConstantValue::getTsCode, Function.identity()));
                constantValueMap.put("HOLD_CODES", tmpMap);
            }
        }
        return constantValueMap;
    }

    private void checkRealData(String[] codes, Map<String, Map<String, EmConstantValue>> stockMap, Map<String, List<BigDecimal>> codePctMap, Map<String, List<String>> logsMap, List<EmRealTimeStock> stockList) {
        initLogsMap();
        String type;
        String nowClock = fixLength(stockList.get(0).getTradeDate().substring(11), 8);
        boolean checkRangeOverLimit = Integer.parseInt(nowClock.trim().substring(0, 2)) > 9 || Integer.parseInt(nowClock.trim().substring(3, 5)) >= 30;
        Map<String, Integer> valueCodeCountMap = new HashMap<>();
        RangeOverCode rangeOverCode = rangeOverCodeService.findByDate(NOW_DAY);
        boolean refreshRangeOverCode = false;
        if (rangeOverCode != null && rangeOverCode.getTradeDate() != null && !rangeOverCode.getTradeDate().isBlank()) {
            List<RangeOverCodeValue> codeValueList = rangeOverCode.getCodeValue();
            if (codeValueList != null && !codeValueList.isEmpty()) {
                valueCodeCountMap = codeValueList.stream().collect(Collectors.toMap(RangeOverCodeValue::getCode, RangeOverCodeValue::getCount));
            }
        } else {
            rangeOverCode = new RangeOverCode();
            rangeOverCode.setTradeDate(NOW_DAY);
        }
        boolean rangeOverLimit;
        boolean highLimit;
        boolean noConcerned;
        boolean holds;
        boolean concerned;
        boolean noBuy;
        boolean yesterdayHigh;
        StringBuilder holdRemark = new StringBuilder();
        for (EmRealTimeStock rt : stockList) {
            holdRemark.setLength(0);
            String tsCode = rt.getTsCode();
            noConcerned = codes[1].contains(tsCode);
            holds = codes[2].contains(tsCode) || (stockMap.containsKey("HOLD_CODES") && stockMap.get("HOLD_CODES").containsKey(tsCode));
            concerned = !holds && (codes[0].contains(tsCode) || (stockMap.containsKey("CONCERN_CODES") && stockMap.get("CONCERN_CODES").containsKey(tsCode)));
            noBuy = codes[3].contains(tsCode);
            yesterdayHigh = !holds && codes[4].contains(tsCode);
            if (!rt.getName().contains("ST") && !rt.getName().contains("退") && tsCode.startsWith("3") && !noConcerned && rt.getPctChg() != null && checkRangeOverLimit) {
                if (codePctMap.containsKey(tsCode)) {
                    codePctMap.get(tsCode).add(rt.getPctChg());
                } else {
                    List<BigDecimal> pchQueue = new LinkedList<>();
                    pchQueue.add(rt.getPctChg());
                    codePctMap.put(tsCode, pchQueue);
                }
                List<BigDecimal> fiveMinutesPch = codePctMap.get(tsCode);
                if (fiveMinutesPch.size() > 10) {
                    fiveMinutesPch.remove(0);
                }
                rangeOverLimit = calRange(fiveMinutesPch);
                if (rangeOverLimit) {
                    valueCodeCountMap.put(tsCode, convertTimeCount(nowClock));
                    refreshRangeOverCode = true;
                }
            }
            rangeOverLimit = valueCodeCountMap.containsKey(tsCode) && convertTimeCount(nowClock) - valueCodeCountMap.get(tsCode) <= 40;
            highLimit = rt.getPriHigh() != null && calRatio(rt.getPriHigh(), rt.getPriClosePre()).compareTo(PCH_LIMIT) > 0;
            if (!noConcerned && !rt.getName().contains("ST") && !rt.getName().contains("退") && tsCode.startsWith("3") && rt.getPctChg() != null && (highLimit || concerned || holds || rangeOverLimit || yesterdayHigh)) {
                type = (concerned ? "C" : holds ? "H" : highLimit ? "F" : !yesterdayHigh ? "R" : "L");
                if ((holds || concerned) && rt.getCurrentPri() != null && (stockMap.containsKey("HOLD_CODES") && stockMap.get("HOLD_CODES").containsKey(tsCode) || stockMap.containsKey("CONCERN_CODES") && stockMap.get("CONCERN_CODES").containsKey(tsCode))) {
                    EmConstantValue emConstantValue = stockMap.containsKey("HOLD_CODES") && stockMap.get("HOLD_CODES").containsKey(tsCode) ? stockMap.get("HOLD_CODES").get(tsCode) : stockMap.get("CONCERN_CODES").get(tsCode);
                    BigDecimal realRatio = calRatio(rt.getCurrentPri(), emConstantValue.getPrice());
                    BigDecimal amount = rt.getCurrentPri().subtract(emConstantValue.getPrice()).multiply(BigDecimal.valueOf(emConstantValue.getVol()));
                    BigDecimal potentialBenefits;
                    holdRemark.append(fixLength(realRatio, 7));
                    if (emConstantValue.getProfit() != null) {
                        potentialBenefits = amount.subtract(emConstantValue.getProfit());
                        holdRemark.append(fixLength("", 10));
                        holdRemark.append(fixLength(potentialBenefits.setScale(2, RoundingMode.HALF_UP), 10));
                    } else {
                        holdRemark.append(fixLength(amount, 10));
                        holdRemark.append(fixLength("", 10));
                    }
                } else {
                    holdRemark.append(fixLength("", 7));
                    holdRemark.append(fixLength("", 10));
                    holdRemark.append(fixLength("", 10));
                }
                BigDecimal avg = amountMap.containsKey(tsCode)
                        ? amountMap.get(tsCode).compareTo(rt.getAmount()) == 0 || rt.getVol() - volMap.get(tsCode) == 0 ? BigDecimal.ZERO : rt.getAmount().subtract(amountMap.get(tsCode)).divide(BigDecimal.valueOf(rt.getVol() - volMap.get(tsCode)).multiply(HUNDRED), 3, RoundingMode.HALF_UP)
                        : rt.getVol() == null ? BigDecimal.ZERO : rt.getAmount().divide(BigDecimal.valueOf(rt.getVol()).multiply(HUNDRED), 3, RoundingMode.HALF_UP);
                long volStep = 0L;
                String volStr = "";
                if (rt.getVol() != null) {
                    if (volMap.containsKey(tsCode)) {
                        volStep = rt.getVol() - volMap.get(tsCode);
                        volStr = fixPositiveLength(volStep, 5);
                        if (volStepMap.get(rt.getTsCode()) != 0) {
                            volStr = fixPositiveLength("(" + calRatio(volStep, volStepMap.get(rt.getTsCode())) + ")", 5) + volStr;
                        }
                    } else {
                        volStep = rt.getVol();
                        volStr = fixPositiveLength(volStep, 10);
                    }
                }
                logsMap.get(type).add(tsCode.substring(2, 6)
                        + fixLength(("N,C".contains(String.valueOf(rt.getName().charAt(0))) ? rt.getName().substring(1, 3) : rt.getName().substring(0, 2)), 3)
                        + fixLength(rt.getPctChg(), 6) + fixLength(rt.getChangeHand(), 5)
                        + holdRemark
                        + fixLength(rt.getCurrentPri(), 6)
                        + fixLength(avg.compareTo(BigDecimal.ZERO) == 0 ? "" : avg, 9)
                        + fixLength(volStr, 11)
                        + fixLength(calBar(rt.getTsCode(), rt.getTradeDate(), rt.getCurrentPri()), 8)
                        + fixLength(rt.getPe(), 8)
                        + fixLength(rt.getCirculationMarketCap().divide(HUNDRED_MILLION, 3, RoundingMode.HALF_UP), 8)
                );
                if (rt.getVol() != null) {
                    volMap.put(tsCode, rt.getVol());
                    amountMap.put(tsCode, rt.getAmount());
                    volStepMap.put(rt.getTsCode(), volStep);
                }
                if (holds && rt.getPriOpen() != null && rt.getPriHigh() != null && stockMap.get("HOLD_CODES").containsKey(tsCode) && stockMap.get("HOLD_CODES").get(tsCode).isSellable()) {
                    //低开超1%,涨超买入价回落卖出
                    if (calRatio(rt.getPriOpen(), rt.getPriClosePre()).compareTo(nOne) < 0 && rt.getPriHigh().compareTo(rt.getPriClosePre()) >= 0 && rt.getPctChg().compareTo(BigDecimal.ZERO) < 0) {
                        MessageUtil.sendNotificationMsg("SELL ONE ", tsCode.substring(2, 6));
                    }
                    //高开超1%且跌落买入价时卖出,或者非封板收盘卖
                    if (calRatio(rt.getPriOpen(), rt.getPriClosePre()).compareTo(BigDecimal.ONE) > 0 && rt.getPctChg().compareTo(BigDecimal.ZERO) < 0) {
                        MessageUtil.sendNotificationMsg("SELL ONE ", tsCode.substring(2, 6));
                    }
                }
                if (!concerned && !holds && rt.getPctChg() != null) {
                    if (codeCountMap.containsKey(tsCode)) {
                        if (rt.getPctChg().compareTo(codeMaxMap.get(tsCode)) == 0) {
                            // max count++;
                            codeCountMap.put(tsCode, codeCountMap.get(tsCode) + 1);
                        } else {
                            if (codeMaxMap.get(tsCode).compareTo(rt.getPctChg()) < 0) {
                                //reset count when meet greater one;
                                codeCountMap.put(tsCode, 0);
                            }
                            //put greater one
                            codeMaxMap.put(tsCode, codeMaxMap.get(tsCode).max(rt.getPctChg()));
                        }
                        if (codeCountMap.get(tsCode) > 3
                                && rt.getPctChg().compareTo(codeMaxMap.get(tsCode)) < 0
                                && !noBuy
                        ) { // 触发涨停板
                            // info && reset count
                            MessageUtil.sendNotificationMsg("BUY ONE ", tsCode.substring(2, 6));
                            codeCountMap.put(tsCode, 0);
                        }
                    } else {
                        // info new
                        codeCountMap.put(tsCode, 0);
                        codeMaxMap.put(tsCode, rt.getPctChg());
                        MessageUtil.sendNotificationMsg("NEW ONE ", tsCode.substring(2, 6));
                    }
                    if (rt.getPctChg().compareTo(PCH_OVER_LIMIT) > 0) { // 17
                        codeOverLimitMap.put(tsCode, codeOverLimitMap.getOrDefault(tsCode, 0) + 1);
                    }
                    if (codeOverLimitMap.getOrDefault(tsCode, 0) > 7
                            && rt.getPctChg().compareTo(codeMaxMap.get(tsCode)) < 0
                            && rt.getPctChg().compareTo(MAX_LIMIT) < 0
                            && !noBuy) {
                        MessageUtil.sendNotificationMsg("BUY LONE ", tsCode.substring(2, 6));
                        codeOverLimitMap.put(tsCode, 0);
                    }
                }
            }
        }
        log.warn(" ____________________________________________________________________________________________________________________________________________");
        String title = "|   TIME   |  I  |" + " T CODE 简称 |  "
                + fixLengthTitle(" RT ", 4)
                + fixLengthTitle(" H ", 3)
                + fixLengthTitle(" RR  ", 5)
                + fixLengthTitle("   AM   ", 8)
                + fixLengthTitle("   PB   ", 8)
                + fixLengthTitle(" CP ", 4)
                + fixLengthTitle("AVG", 7)
                + fixLengthTitle("VOL", 9)
                + fixLengthTitle("BAR", 6)
                + fixLengthTitle(" PE ", 6)
                + fixLengthTitle(" CM ", 6);
        log.warn(title.substring(0, title.length() - 2));
        log.warn(" ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
        printMapInfo(logsMap, nowClock);
        log.warn(" ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾");
        if (!valueCodeCountMap.isEmpty() && refreshRangeOverCode) {
            List<RangeOverCodeValue> valueList = valueCodeCountMap.entrySet().stream().map(x -> new RangeOverCodeValue(x.getKey(), x.getValue())).toList();
            rangeOverCode.setCodeValue(valueList);
            rangeOverCodeService.saveEntity(rangeOverCode);
        }
    }

    private int convertTimeCount(String nowClock) {
        return Integer.parseInt(nowClock.substring(0, 2)) * 60 * 2
                + Integer.parseInt(nowClock.substring(3, 5)) * 2
                + (Integer.parseInt(nowClock.substring(6, 8)) >= 30 ? 1 : 0)
                - 1140;
    }

    private void printMapInfo(Map<String, List<String>> logsMap, String nowClock) {
        Arrays.stream(CODE_TYPE.split(",")).forEach(x -> {
            if (!logsMap.get(x).isEmpty()) {
                for (int i = 0; i < logsMap.get(x).size(); i++) {
                    log.warn("| " + nowClock + fixLength(i + 1 + " ", 3) + x + " " + logsMap.get(x).get(i));
                }
            }
        });
    }

    private String fixLength(Object str, int length) {
        return String.format("%" + length + "s", str) + " | ";
    }

    private String fixLengthTitle(Object str, int length) {
        return String.format("%" + length + "s", str) + "  |  ";
    }


    @SuppressWarnings("unused")
    private String fixLengthNegative(Object str, int length) {
        return String.format("%-" + length + "s", str);
    }

    private String fixPositiveLength(Object str, int length) {
        return String.format("%" + length + "s", str);
    }

    private boolean calRange(List<BigDecimal> values) {
//        BigDecimal maxValue = Collections.max(values);
        BigDecimal maxValue = values.get(values.size() - 1);
        BigDecimal minValue = Collections.min(values);
        return maxValue.subtract(minValue).compareTo(RANGE_LIMIT) >= 0 && values.indexOf(maxValue) > values.indexOf(minValue);
    }

    private boolean isTradeHour() {
        LocalDateTime n = LocalDateTime.now();
        String res = String.format("%02d", n.getHour()) + String.format("%02d", n.getMinute());
        int tmp = Integer.parseInt(res);
        return tmp >= 915 && tmp < 1131 || tmp >= 1300 && tmp < 1501;
    }

    private BigDecimal calRatio(BigDecimal curClosePri, BigDecimal doorPri) {
        return curClosePri.subtract(doorPri).multiply(HUNDRED).divide(doorPri, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calRatio(Long curValue, Long doorValue) {
        return BigDecimal.valueOf(curValue)
                .divide(BigDecimal.valueOf(doorValue), 1, RoundingMode.HALF_UP);
    }

    private BigDecimal calBar(String tsCode, String tradeDate, BigDecimal curPri) {
        RealBar newBar = new RealBar();
        RealBar lastOne = realBarService.findOne(tradeDate, tsCode);
        if (curPri == null || lastOne == null) {
            List<EmRealTimeStock> emRealTimeStocks = emRealTimeStockService.findStocksByCode(tsCode);
            List<BigDecimal> priceList = emRealTimeStocks.stream().map(EmRealTimeStock::getCurrentPri).filter(Objects::nonNull).toList();
            List<BigDecimal> shortSmaPriceList = calSMA(priceList, SHORT_PERIOD);
            List<BigDecimal> longSmaPriceList = calSMA(priceList, LONG_PERIOD);
            List<BigDecimal> difList = getDifList(shortSmaPriceList, longSmaPriceList);
            List<BigDecimal> deaList = calSMA(difList, MID_PERIOD);
            BigDecimal bar = difList.getLast().subtract(deaList.getLast()).multiply(BigDecimal.TWO);
            newBar.setShortSmaPrice(shortSmaPriceList.getLast());
            newBar.setLongSmaPrice(longSmaPriceList.getLast());
            newBar.setDif(difList.getLast());
            newBar.setDea(deaList.getLast());
            newBar.setBar(bar);
        } else {
            BigDecimal shortSmaPrice = calSMA(curPri, lastOne.getShortSmaPrice(), SHORT_PERIOD);
            BigDecimal longSmaPrice = calSMA(curPri, lastOne.getShortSmaPrice(), LONG_PERIOD);
            BigDecimal dif = shortSmaPrice.subtract(longSmaPrice);
            BigDecimal dea = calSMA(dif, lastOne.getDea(), MID_PERIOD);
            BigDecimal bar = dif.subtract(dea).multiply(BigDecimal.TWO);
            newBar.setShortSmaPrice(shortSmaPrice);
            newBar.setLongSmaPrice(longSmaPrice);
            newBar.setDif(dif);
            newBar.setDea(dea);
            newBar.setBar(bar);
        }
        newBar.setTradeDate(tradeDate);
        newBar.setTsCode(tsCode);
        return realBarService.save(newBar).getBar();
    }

    private static List<BigDecimal> getDifList(List<BigDecimal> shortSmaPriceList, List<BigDecimal> longSmaPriceList) {
        int minSize = Math.min(shortSmaPriceList.size(), longSmaPriceList.size());
        int shortListSize = shortSmaPriceList.size();
        int longListSize = longSmaPriceList.size();
        List<BigDecimal> difList = new ArrayList<>();
        for (int i = minSize; i >= 0; i--) {
            BigDecimal dif = shortSmaPriceList.get(shortListSize - minSize)
                    .subtract(longSmaPriceList.get(longListSize - minSize));
            difList.add(dif);
        }
        return difList;
    }

    private List<BigDecimal> calSMA(List<BigDecimal> values, int n) {
        return CalculateUtils.calSMA(values, n, 2);
    }

    private BigDecimal calSMA(BigDecimal curValue, BigDecimal oldValue, int n) {
        return CalculateUtils.calSMANext(curValue, oldValue, n, 2);
    }

    @Autowired
    public void setEmRealTimeStockService(EmRealTimeStockService emRealTimeStockService) {
        this.emRealTimeStockService = emRealTimeStockService;
    }

    @Autowired
    public void setEmConstantService(EmConstantService emConstantService) {
        this.emConstantService = emConstantService;
    }

    @Autowired
    public void setRangeOverCodeService(RangeOverCodeService rangeOverCodeService) {
        this.rangeOverCodeService = rangeOverCodeService;
    }

    @Autowired
    public void setRealBarService(RealBarService realBarService) {
        this.realBarService = realBarService;
    }
}
