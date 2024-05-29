package com.stock.pycurrent.schedule;

import com.stock.pycurrent.entity.*;
import com.stock.pycurrent.entity.jsonvalue.EmConstantValue;
import com.stock.pycurrent.entity.jsonvalue.LimitCodeValue;
import com.stock.pycurrent.entity.jsonvalue.RangeOverCodeValue;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.service.*;
import com.stock.pycurrent.util.CalculateUtils;
import com.stock.pycurrent.util.MessageUtil;
import com.stock.pycurrent.util.PARAMS;
import com.stock.pycurrent.util.StockUtils;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
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
import java.util.stream.Stream;

@Configuration
@CommonsLog
public class PullData implements CommandLineRunner {

    private static final int CHAR_LENGTH = 128;
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);
    private static final BigDecimal PCH_LIMIT = BigDecimal.valueOf(15);
    private static final BigDecimal FIFTEEN = BigDecimal.valueOf(15);
    private static final BigDecimal SIXTEEN = BigDecimal.valueOf(16);
    private static final BigDecimal SEVENTEEN = BigDecimal.valueOf(17);
    private static final BigDecimal EIGHTEEN = BigDecimal.valueOf(18);
    private static final BigDecimal NINETEEN = BigDecimal.valueOf(19);
    private static final BigDecimal RANGE_LIMIT = BigDecimal.valueOf(6);
    private static final BigDecimal HUNDRED_MILLION = BigDecimal.valueOf(100000000);

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");


    private static final String CODE_TYPE = "F,A,R,C,L,H";
    private static final String CODE_PRINT_TYPE = "F,A,C,L,H";
    @Resource
    private EmRealTimeStockService emRealTimeStockService;
    @Resource
    private EmConstantService emConstantService;
    @Resource
    private RangeOverCodeService rangeOverCodeService;
    @Resource
    private RealBarService realBarService;
    @Resource
    private LimitCodeService limitCodeService;

    @Resource
    private CurCountService curCountService;

    private final Map<String, Integer> codeCountMap = new HashMap<>();

    private final Map<String, List<BigDecimal>> codePctMap = new HashMap<>();

    private final Map<String, List<String>> logsMap = new HashMap<>();

    private final Map<String, Long> volMap = new HashMap<>();
    private final Map<String, Long> volStepMap = new HashMap<>();


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

    @Scheduled(cron = "58 * 9-16 * * ?")
    public void pullRealTimeData() {
        if (isTradeHour() && StockUtils.isNotRest() && !PARAMS.BAK_MODE) {
            List<EmRealTimeStock> stockList = emRealTimeStockService.findEmCurrent();
            List<EmConstant> emConstants = emConstantService.findAll();
            String[] codes = prepareConstantsCodes(emConstants);
            Map<String, Map<String, EmConstantValue>> stockMap = prepareConstantsMap(emConstants);

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
        String NOW_DAY = LocalDateTime.now().format(DATE_TIME_FORMAT);
        initLogsMap();
        String type;
        String nowClock = fixLength(stockList.get(0).getTradeDate().substring(11), 8);
        int nowHour = Integer.parseInt(nowClock.trim().substring(0, 2));
        int nowMinute = Integer.parseInt(nowClock.trim().substring(3, 5));

        boolean checkOverLimit = nowHour > 9 || nowMinute >= 30;

        Map<String, Integer> valueCodeCountMap = new HashMap<>();
        RangeOverCode rangeOverCode = rangeOverCodeService.findByDate(NOW_DAY);
        boolean refreshRangeOverCode = false;
        if (rangeOverCode != null && rangeOverCode.getTradeDate() != null && !rangeOverCode.getTradeDate().isBlank()) {
            List<RangeOverCodeValue> codeValueList = rangeOverCode.getCodeValue();
            if (codeValueList != null && !codeValueList.isEmpty()) {
                valueCodeCountMap = codeValueList.stream().collect(Collectors.toMap(RangeOverCodeValue::getCode, RangeOverCodeValue::getCount));
            }
        } else if (checkOverLimit) {
            rangeOverCode = new RangeOverCode();
            rangeOverCode.setTradeDate(NOW_DAY);
        }

        Map<String, LimitCodeValue> limitCodeMap = new HashMap<>();
        LimitCode lastOne = limitCodeService.findLastOne(NOW_DAY);
        if (lastOne != null && lastOne.getTradeDate() != null && !lastOne.getTradeDate().isBlank()) {
            List<LimitCodeValue> codeValueList = lastOne.getCodeValue();
            if (codeValueList != null && !codeValueList.isEmpty()) {
                limitCodeMap = codeValueList.stream().collect(Collectors.toMap(LimitCodeValue::getCode, Function.identity()));
            }
        }
        boolean rangeOverLimit;
        boolean highLimit;
        boolean noConcerned;
        boolean holds;
        boolean concerned;
        boolean yesterdayHigh;

        EmConstant emConstant = emConstantService.findOneByKey();
        boolean notification = "TRUE".equals(emConstant.getCValue());

        StringBuilder holdRemark = new StringBuilder();
        for (EmRealTimeStock rt : stockList) {
            String tsCode = rt.getTsCode();
            holds = codes[2].contains(tsCode) || (stockMap.containsKey("HOLD_CODES") && stockMap.get("HOLD_CODES").containsKey(tsCode));
            concerned = !holds && (codes[0].contains(tsCode) || (stockMap.containsKey("CONCERN_CODES") && stockMap.get("CONCERN_CODES").containsKey(tsCode)));
            if (tsCode.startsWith("30")) {
                boolean peFlag = rt.getPe() == null || rt.getPe().compareTo(BigDecimal.ZERO) < 0;
                boolean cmFlag = rt.getCirculationMarketCap() == null || rt.getCirculationMarketCap().compareTo(Constants.FOUR_BILLION) < 0;
                if ((peFlag || cmFlag) && !concerned && !holds) {
                    continue;
                }
            }
            String tsName = rt.getName();
            if (tsName.contains("*ST") || tsName.contains("ST")) {
                tsName = tsName.replace("*ST", "");
                tsName = tsName.replace("ST", "");
            }
            holdRemark.setLength(0);
            noConcerned = codes[1].contains(tsCode);

            yesterdayHigh = limitCodeMap.containsKey(tsCode);

            if (!tsCode.startsWith("30") && !concerned && !holds) {
                continue;
            }

            String nameFirst = "N,C".contains(String.valueOf(tsName.charAt(0))) ? tsName.substring(1, 3) : tsName.substring(0, 2);
            if (rt.getCurrentPri() != null
                && (tsCode.startsWith("0") || tsCode.startsWith("60"))
                && !tsName.contains("退")
                && !noConcerned
                && (limitCodeMap.containsKey(tsCode) && (limitCodeMap.get(tsCode).getCount() >= 2)
                    || holds
                    || concerned)) {
                logsMap.get(holds ? "H" : concerned && !tsCode.startsWith("30") ? "C" : "A").add(fixPositiveLength(limitCodeMap.containsKey(tsCode) ? limitCodeMap.get(tsCode).getCount() : "", 2) + " " + (tsCode.charAt(0) + tsCode.substring(2, 6)) + fixLength(nameFirst, 3) + fixLength(rt.getPctChg(), 6) + fixLength(rt.getChangeHand(), 5) + fixLength("", 7) + fixLength("", 7) + fixLength(rt.getCurrentPri(), 6) + fixLength("", 11) + fixLength(rt.getVol() != null && checkOverLimit ? calBar(rt.getTsCode(), rt.getTradeDate(), rt.getCurrentPri()).multiply(THOUSAND).setScale(0, RoundingMode.FLOOR) : "", 8) + fixLength(rt.getCirculationMarketCap().divide(HUNDRED_MILLION, 3, RoundingMode.HALF_UP), 8) + fixLength(rt.getPe(), 8));
                if (!holds && !concerned) {
                    sendNewNotification(notification, rt, tsCode);
                }
            }

            if (!tsName.contains("退") && tsCode.startsWith("3") && !noConcerned && rt.getPctChg() != null && checkOverLimit) {
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
            rangeOverLimit = valueCodeCountMap.containsKey(tsCode) && (convertTimeCount(nowClock) - valueCodeCountMap.get(tsCode) <= 40 || rt.getPctChg().compareTo(BigDecimal.TEN) >= 0);
            highLimit = rt.getPriHigh() != null && calRatio(rt.getPriHigh(), rt.getPriClosePre()).compareTo(PCH_LIMIT) > 0;
            if (!noConcerned
                && !tsName.contains("退")
                && tsCode.startsWith("3")
                && rt.getPctChg() != null
                && (highLimit || concerned || holds || rangeOverLimit || yesterdayHigh)) {
                type = (concerned ? "C" : holds ? "H" : highLimit ? "F" : !yesterdayHigh ? "R" : "L");
                if ((holds || concerned) && rt.getCurrentPri() != null && (stockMap.containsKey("HOLD_CODES") && stockMap.get("HOLD_CODES").containsKey(tsCode) || stockMap.containsKey("CONCERN_CODES") && stockMap.get("CONCERN_CODES").containsKey(tsCode))) {
                    EmConstantValue emConstantValue = stockMap.containsKey("HOLD_CODES") && stockMap.get("HOLD_CODES").containsKey(tsCode) ? stockMap.get("HOLD_CODES").get(tsCode) : stockMap.get("CONCERN_CODES").get(tsCode);
                    BigDecimal realRatio = calRatio(rt.getCurrentPri(), emConstantValue.getPrice());

                    holdRemark.append(fixLength(realRatio, 7));
                    if (emConstantValue.getProfit() != null) {
                        holdRemark.append(fixLength(emConstantValue.getPrice(), 7));
                    } else {
                        holdRemark.append(fixLength("", 7));
                    }
                } else {
                    holdRemark.append(fixLength("", 7));
                    holdRemark.append(fixLength("", 7));
                }

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
                logsMap.get(type).add(getPeekDesc(rt) + (yesterdayHigh ? limitCodeMap.get(tsCode).getCount() : rangeOverLimit ? "R" : " ") + " " + tsCode.charAt(0) + tsCode.substring(2, 6) + fixLength(nameFirst, 3) + fixLength(rt.getPctChg(), 6) + fixLength(rt.getChangeHand(), 5) + holdRemark + fixLength(rt.getCurrentPri(), 6) + fixLength(volStr, 11) + fixLength(rt.getVol() != null && checkOverLimit ? calBar(rt.getTsCode(), rt.getTradeDate(), rt.getCurrentPri()).multiply(THOUSAND).setScale(0, RoundingMode.FLOOR) : "", 8) + fixLength(rt.getCirculationMarketCap().divide(HUNDRED_MILLION, 3, RoundingMode.HALF_UP), 8) + fixLength(rt.getPe(), 8));
                if (rt.getVol() != null) {
                    volMap.put(tsCode, rt.getVol());
                    volStepMap.put(rt.getTsCode(), volStep);
                }
                if (highLimit) {
                    sendNewNotification(notification, rt, tsCode);
                }
            }
        }
        log.warn(StockUtils.concatChar("_", CHAR_LENGTH));
        String title = "|   TIME   |  I  |" + " T     CODE 简称 |  " + fixLengthTitle(" RT ", 4) + fixLengthTitle(" H ", 3) + fixLengthTitle(" RR  ", 5) + fixLengthTitle(" BP ", 5) + fixLengthTitle(" CP ", 4) + fixLengthTitle("VOL", 9) + fixLengthTitle("BAR", 6) + fixLengthTitle(" CM ", 6) + fixLengthTitle(" PE ", 6);
        log.warn(title.substring(0, title.length() - 2));
        log.warn(StockUtils.concatChar("‾", CHAR_LENGTH));
        printMapInfo(logsMap, nowClock);
        log.warn(StockUtils.concatChar("‾", CHAR_LENGTH));
        if (!valueCodeCountMap.isEmpty() && refreshRangeOverCode) {
            List<RangeOverCodeValue> valueList = valueCodeCountMap.entrySet().stream().map(x -> new RangeOverCodeValue(x.getKey(), x.getValue())).toList();
            rangeOverCode.setCodeValue(valueList);
            rangeOverCodeService.saveEntity(rangeOverCode);
        }

        if (nowMinute % 10 == 5 || nowMinute % 10 == 0
            || nowHour == 9 && nowMinute == 16
            || nowHour == 15 && nowMinute == 1) {
            CurCount curCount = statisticsCurCount(stockList);
            curCountService.saveOne(curCount);
        }

    }

    private void sendNewNotification(boolean notification, EmRealTimeStock rt, String tsCode) {
        if (rt.getPctChg() != null) {
            if (codeCountMap.containsKey(tsCode)) {
                if (CalculateUtils.reachTwentyLimit(rt)) {
                    // max count++;
                    codeCountMap.put(tsCode, codeCountMap.getOrDefault(tsCode, 0) + 1);
                    if (codeCountMap.get(tsCode) < 2) {
                        sendNotificationMsg("LIMIT ONE ", tsCode, notification);
                    }
                }
            } else {
                codeCountMap.put(tsCode, 0);
                sendNotificationMsg("NEW ONE ", tsCode, notification);
            }

        }
    }

    private CurCount statisticsCurCount(List<EmRealTimeStock> stockList) {
        Integer[] countArray = Stream.generate(() -> 0).limit(30).toArray(Integer[]::new);
        for (EmRealTimeStock em : stockList) {
            if (em.getPctChg() != null) {
                if (em.getTsCode().startsWith("00")) {
                    countArray[0]++;
                    if (em.getPctChg().compareTo(BigDecimal.ZERO) >= 0) {
                        countArray[1]++;
                    }
                    if (em.getPctChg().compareTo(Constants.FIVE) >= 0) {
                        countArray[2]++;
                    } else if (em.getPctChg().compareTo(Constants.THREE) >= 0) {
                        countArray[4]++;
                    } else if (em.getPctChg().compareTo(BigDecimal.ONE) >= 0) {
                        countArray[5]++;
                    } else if (em.getPctChg().compareTo(BigDecimal.ZERO) >= 0) {
                        countArray[6]++;
                    } else if (em.getPctChg().compareTo(Constants.N_ONE) >= 0) {
                        countArray[7]++;
                    } else if (em.getPctChg().compareTo(Constants.N_THREE) >= 0) {
                        countArray[8]++;
                    } else if (em.getPctChg().compareTo(Constants.N_SEVEN) >= 0) {
                        countArray[9]++;
                    } else {
                        countArray[3]++;
                    }
                } else if (em.getTsCode().startsWith("30")) {
                    countArray[10]++;
                    if (em.getPctChg().compareTo(BigDecimal.ZERO) >= 0) {
                        countArray[11]++;
                    }
                    if (em.getPctChg().compareTo(Constants.FIVE) >= 0) {
                        countArray[12]++;
                    } else if (em.getPctChg().compareTo(Constants.THREE) >= 0) {
                        countArray[14]++;
                    } else if (em.getPctChg().compareTo(BigDecimal.ONE) >= 0) {
                        countArray[15]++;
                    } else if (em.getPctChg().compareTo(BigDecimal.ZERO) >= 0) {
                        countArray[16]++;
                    } else if (em.getPctChg().compareTo(Constants.N_ONE) >= 0) {
                        countArray[17]++;
                    } else if (em.getPctChg().compareTo(Constants.N_THREE) >= 0) {
                        countArray[18]++;
                    } else if (em.getPctChg().compareTo(Constants.N_SEVEN) >= 0) {
                        countArray[19]++;
                    } else {
                        countArray[13]++;
                    }
                } else if (em.getTsCode().startsWith("60")) {
                    countArray[20]++;
                    if (em.getPctChg().compareTo(BigDecimal.ZERO) >= 0) {
                        countArray[21]++;
                    }
                    if (em.getPctChg().compareTo(Constants.FIVE) >= 0) {
                        countArray[22]++;
                    } else if (em.getPctChg().compareTo(Constants.THREE) >= 0) {
                        countArray[24]++;
                    } else if (em.getPctChg().compareTo(BigDecimal.ONE) >= 0) {
                        countArray[25]++;
                    } else if (em.getPctChg().compareTo(BigDecimal.ZERO) >= 0) {
                        countArray[26]++;
                    } else if (em.getPctChg().compareTo(Constants.N_ONE) >= 0) {
                        countArray[27]++;
                    } else if (em.getPctChg().compareTo(Constants.N_THREE) >= 0) {
                        countArray[28]++;
                    } else if (em.getPctChg().compareTo(Constants.N_SEVEN) >= 0) {
                        countArray[29]++;
                    } else {
                        countArray[23]++;
                    }
                }
            }
        }
        return new CurCount(stockList.get(0).getTradeDate(), countArray);
    }

    private int convertTimeCount(String nowClock) {
        return Integer.parseInt(nowClock.substring(0, 2)) * 60 * 2 + Integer.parseInt(nowClock.substring(3, 5)) * 2 + (Integer.parseInt(nowClock.substring(6, 8)) >= 30 ? 1 : 0) - 1140;
    }

    private void printMapInfo(Map<String, List<String>> logsMap, String nowClock) {
        Arrays.stream(CODE_PRINT_TYPE.split(",")).forEach(x -> {
            if (!logsMap.get(x).isEmpty()) {
                List<String> remarks = logsMap.get(x);
                remarks.sort((o1, o2) -> {
                    char o11 = o1.charAt(1);
                    char o21 = o2.charAt(1);
                    if (o11 <= 57 && o11 >= 49 && o21 <= 57 && o21 >= 49) {
                        if (o21 == o11) {
                            char o10 = o1.charAt(0);
                            char o20 = o2.charAt(0);
                            return o20 - o10;
                        }
                        return o21 - o11;
                    } else {
                        if (o11 <= 57 && o11 >= 49) {
                            return -1;
                        } else if (o21 <= 57 && o21 >= 49) {
                            return 1;
                        }
                        char o10 = o1.charAt(0);
                        char o20 = o2.charAt(0);
                        if (o10 == o20) {
                            return 0;
                        } else if (o10 == 'P') {
                            return -1;
                        } else if (o20 == 'P') {
                            return 1;
                        } else {
                            return o20 - o10;
                        }
                    }
                });

                for (int i = 0; i < remarks.size(); i++) {
                    log.warn("| " + nowClock + fixLength(i + 1 + " ", 3) + x + " " + remarks.get(i));
                }
            }
        });
    }

    private void sendNotificationMsg(String title, String code, Boolean notification) {
        if (notification) {
            MessageUtil.sendNotificationMsg(title, code);
        } else {
            log.warn(title + " " + code);
        }
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
        if (limitCodeService.checkDateHoliday(String.format("%02d", n.getMonthValue()) + "-" + String.format("%02d", n.getDayOfMonth()))) {
            return false;
        }
        String res = String.format("%02d", n.getHour()) + String.format("%02d", n.getMinute());
        int tmp = Integer.parseInt(res);
        return tmp >= 915 && tmp < 1131 || tmp >= 1300 && tmp < 1501;
    }

    private BigDecimal calRatio(BigDecimal curClosePri, BigDecimal doorPri) {
        return curClosePri.subtract(doorPri).multiply(HUNDRED).divide(doorPri, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calRatio(Long curValue, Long doorValue) {
        return BigDecimal.valueOf(curValue).divide(BigDecimal.valueOf(doorValue), 1, RoundingMode.HALF_UP);
    }

    private BigDecimal calBar(String tsCode, String tradeDate, BigDecimal curPri) {
        RealBar newBar = new RealBar();
        RealBar lastBar = realBarService.findOne(tradeDate, tsCode);
        if (curPri == null || lastBar == null) {
            List<EmRealTimeStock> emRealTimeStockList = emRealTimeStockService.findRBarStockByCode(tsCode);
            if (emRealTimeStockList != null && !emRealTimeStockList.isEmpty()) {
                BigDecimal lastBarValue = BigDecimal.ZERO;
                EmRealTimeStock cur = emRealTimeStockList.get(0);
                newBar.setShortSmaPrice(cur.getCurrentPri());
                newBar.setLongSmaPrice(cur.getCurrentPri());
                newBar.setDif(BigDecimal.ZERO);
                newBar.setDea(BigDecimal.ZERO);
                newBar.setBar(BigDecimal.ZERO);
                newBar.setCurPri(cur.getCurrentPri());
                newBar.setTradeDate(cur.getTradeDate());
                newBar.setTsCode(tsCode);
                realBarService.save(newBar);
                if (emRealTimeStockList.size() > 1) {
                    for (int i = 1; i < emRealTimeStockList.size(); i++) {
                        cur = emRealTimeStockList.get(i);
                        lastBarValue = calBar(tsCode, cur.getTradeDate(), cur.getCurrentPri());
                    }
                }
                return lastBarValue;
            } else {
                newBar.setShortSmaPrice(curPri);
                newBar.setLongSmaPrice(curPri);
                newBar.setDif(BigDecimal.ZERO);
                newBar.setDea(BigDecimal.ZERO);
                newBar.setBar(BigDecimal.ZERO);
                newBar.setCurPri(curPri);
                newBar.setTradeDate(tradeDate);
                newBar.setTsCode(tsCode);
                lastBar = realBarService.save(newBar);
                return lastBar.getBar();
            }
        } else {
            newBar.setShortSmaPrice(CalculateUtils.calShortEMANext(curPri, lastBar.getShortSmaPrice()));
            newBar.setLongSmaPrice(CalculateUtils.calLongEMANext(curPri, lastBar.getLongSmaPrice()));
            newBar.setDif(newBar.getShortSmaPrice().subtract(newBar.getLongSmaPrice()));
            newBar.setDea(CalculateUtils.calMidEMANext(newBar.getDif(), lastBar.getDif()));
            newBar.setBar(newBar.getDif().subtract(newBar.getDea()).multiply(BigDecimal.TWO));
            newBar.setCurPri(curPri);
            newBar.setTradeDate(tradeDate);
            newBar.setTsCode(tsCode);
            return realBarService.save(newBar).getBar();
        }
    }

    private String getPeekDesc(EmRealTimeStock rt) {
        if (rt.getPriHigh() != null) {
            BigDecimal highRatio = calRatio(rt.getPriHigh(), rt.getPriClosePre());
            if (CalculateUtils.reachTwentyLimit(rt)) {
                return "P";
            } else if (highRatio.compareTo(NINETEEN) >= 0) {
                return "9";
            } else if (highRatio.compareTo(EIGHTEEN) >= 0) {
                return "8";
            } else if (highRatio.compareTo(SEVENTEEN) >= 0) {
                return "7";
            } else if (highRatio.compareTo(SIXTEEN) >= 0) {
                return "6";
            } else if (highRatio.compareTo(FIFTEEN) >= 0) {
                return "5";
            }
        }
        return " ";
    }

}
