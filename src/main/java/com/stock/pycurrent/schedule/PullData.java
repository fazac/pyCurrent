package com.stock.pycurrent.schedule;

import com.stock.pycurrent.entity.*;
import com.stock.pycurrent.entity.emum.SSEMsgEnum;
import com.stock.pycurrent.entity.jsonvalue.EmConstantValue;
import com.stock.pycurrent.entity.jsonvalue.LimitCodeValue;
import com.stock.pycurrent.entity.jsonvalue.RangeOverCodeValue;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.entity.vo.RealTimeVO;
import com.stock.pycurrent.service.*;
import com.stock.pycurrent.util.*;
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

    private static final int CHAR_LENGTH = 98;
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
    private static final String CODE_PRINT_TYPE = "F,R,C,L,H";
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
    private BoardCodeService boardCodeService;

    @Resource
    private CurCountService curCountService;
    @Resource
    private CurConcernCodeService curConcernCodeService;

    private final Map<String, Integer> codeCountMap = new HashMap<>();

    private final Map<String, List<BigDecimal>> codePctMap = new HashMap<>();

    private final Map<String, List<String>> logsMap = new HashMap<>();


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

    @Scheduled(cron = "5 * 9-16 * * ?")
    public void pullRealTimeData() {
        if (isTradeHour() && StockUtils.isNotRest() && !PARAMS.BAK_MODE) {
            List<EmRealTimeStock> stockList = emRealTimeStockService.findEmCurrent();
            PrepareData.refreshRTMap(stockList);
            List<EmConstant> emConstants = emConstantService.findAll();
            String[] codes = prepareConstantsCodes(emConstants);
            Map<String, Map<String, EmConstantValue>> stockMap = prepareConstantsMap(emConstants);

            checkRealData(codes, stockMap, codePctMap, logsMap, stockList);
        }
    }


    @SneakyThrows
    public void pullTest() {
        int count = 0;
        do {
            List<EmConstant> emConstants = emConstantService.findAll();
            String[] codes = prepareConstantsCodes(emConstants);
            Map<String, Map<String, EmConstantValue>> stockMap = prepareConstantsMap(emConstants);
            List<String> times = emRealTimeStockService.findTradeDates();
            for (String s : times) {
                Thread.sleep(5000);
                List<EmRealTimeStock> stockList = emRealTimeStockService.findStockByDate(s);
                checkRealData(codes, stockMap, codePctMap, logsMap, stockList);
            }
            count++;
        } while (count != 10);
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
        if (stockList == null || stockList.isEmpty()) {
            return;
        }
        String NOW_DAY = LocalDateTime.now().format(DATE_TIME_FORMAT);
        initLogsMap();
        String type;
        String curTradeDate = stockList.getFirst().getTradeDate();

        String nowClock = fixLength(curTradeDate.substring(11), 8);
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

        BoardCode boardCode = boardCodeService.findByDate(NOW_DAY);

        List<String> todayBoardCodes = new ArrayList<>();
        if (boardCode != null) {
            if (!boardCode.getCodeValue().isBlank()) {
                todayBoardCodes = new ArrayList<>(Arrays.asList(boardCode.getCodeValue().split(",")));
            }
        } else if (checkOverLimit) {
            boardCode = new BoardCode();
            boardCode.setTradeDate(NOW_DAY);
        }


        boolean rangeOverLimit;
        boolean highLimit;
        boolean noConcerned;
        boolean holds;
        boolean concerned;
        boolean yesterdayHigh;
        boolean onboard;

        EmConstant emConstant = emConstantService.findOneByKey();
        boolean notification = "TRUE".equals(emConstant.getCValue());

        StringBuilder holdRemark = new StringBuilder();
        List<CurConcernCode> curConcernCodeList = new ArrayList<>();
        for (EmRealTimeStock rt : stockList) {
            String tsCode = rt.getTsCode();
            holds = codes[2].contains(tsCode) || (stockMap.containsKey("HOLD_CODES") && stockMap.get("HOLD_CODES").containsKey(tsCode));
            concerned = !holds && (codes[0].contains(tsCode) || (stockMap.containsKey("CONCERN_CODES") && stockMap.get("CONCERN_CODES").containsKey(tsCode)));
            boolean threeLinked = true;
            if (tsCode.startsWith("30")) {
                boolean peFlag = rt.getPe() == null || rt.getPe().compareTo(BigDecimal.ZERO) < 0;
                boolean cmFlag = rt.getCirculationMarketCap() == null || rt.getCirculationMarketCap().compareTo(Constants.FOUR_BILLION) < 0;
                if ((peFlag || cmFlag) && !concerned && !holds) {
                    threeLinked = false;
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

            if (rt.getCurrentPri() != null
                && (tsCode.startsWith("0") || tsCode.startsWith("60"))
                && !tsName.contains("退")
                && !noConcerned
                && (limitCodeMap.containsKey(tsCode) && (limitCodeMap.get(tsCode).getCount() >= 2)
                    || holds
                    || concerned)) {
                logsMap.get(holds ? "H" : concerned && !tsCode.startsWith("30") ? "C" : "A").add(fixPositiveLength(limitCodeMap.containsKey(tsCode) ? limitCodeMap.get(tsCode).getCount() : "") + " " + (tsCode.substring(2, 6)) + fixLength("", 1) + fixLength(rt.getPctChg(), 6) + fixLength(rt.getChangeHand(), 5) + fixLength("", 7) + fixLength("", 7) + fixLength(rt.getCurrentPri(), 6) + fixLength(rt.getVol() != null && checkOverLimit ? deleteOrCalBar(rt.getTsCode(), rt.getTradeDate(), rt.getCurrentPri()).multiply(THOUSAND).setScale(0, RoundingMode.FLOOR) : "", 8) + fixLength(rt.getCirculationMarketCap().divide(HUNDRED_MILLION, 3, RoundingMode.HALF_UP), 8) + fixLength(rt.getPe(), 8));
                CurConcernCode curConcernCode = new CurConcernCode();
                curConcernCode.setTsCode(tsCode);
                if (limitCodeMap.containsKey(tsCode)) {
                    curConcernCode.setMark("A " + (limitCodeMap.containsKey(tsCode) ? limitCodeMap.get(tsCode).getCount() : ""));
                } else {
                    curConcernCode.setMark("C");
                }
                if (concerned) {
                    curConcernCode.setTableShow(true);
                }
                curConcernCode.setRt(rt.getPctChg());
                curConcernCode.setH(rt.getChangeHand());
                curConcernCode.setCp(rt.getCurrentPri());
                curConcernCode.setCm(rt.getCirculationMarketCap().divide(HUNDRED_MILLION, 3, RoundingMode.HALF_UP));
                curConcernCode.setPe(rt.getPe());
                curConcernCode.setTradeDate(curTradeDate);
                curConcernCodeList.add(curConcernCode);
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
                    fiveMinutesPch.removeFirst();
                }
                rangeOverLimit = calRange(fiveMinutesPch);
                if (rangeOverLimit) {
                    valueCodeCountMap.put(tsCode, convertTimeCount(nowClock));
                    refreshRangeOverCode = true;
                }
            }
            rangeOverLimit = valueCodeCountMap.containsKey(tsCode) && (convertTimeCount(nowClock) - valueCodeCountMap.get(tsCode) <= 40 || rt.getPctChg().compareTo(BigDecimal.TEN) >= 0);
            highLimit = rt.getPriHigh() != null && calRatio(rt.getPriHigh(), rt.getPriClosePre()).compareTo(PCH_LIMIT) > 0;
            onboard = todayBoardCodes.contains(tsCode);
            if (!noConcerned
                && !tsName.contains("退")
                && tsCode.startsWith("3")
                && rt.getPctChg() != null
                && (highLimit || concerned || holds || rangeOverLimit || yesterdayHigh || onboard)) {
                type = (concerned ? "C" : holds ? "H" : highLimit ? "F" : !yesterdayHigh ? "R" : "L");
                CurConcernCode curConcernCode = new CurConcernCode();
                if ((holds || concerned) && rt.getCurrentPri() != null && (stockMap.containsKey("HOLD_CODES") && stockMap.get("HOLD_CODES").containsKey(tsCode) || stockMap.containsKey("CONCERN_CODES") && stockMap.get("CONCERN_CODES").containsKey(tsCode))) {
                    EmConstantValue emConstantValue = stockMap.containsKey("HOLD_CODES") && stockMap.get("HOLD_CODES").containsKey(tsCode) ? stockMap.get("HOLD_CODES").get(tsCode) : stockMap.get("CONCERN_CODES").get(tsCode);
                    BigDecimal realRatio = calRatio(rt.getCurrentPri(), emConstantValue.getPrice());

                    curConcernCode.setRr(realRatio);
                    holdRemark.append(fixLength(realRatio, 7));
                    if (emConstantValue.getPrice() != null) {
                        holdRemark.append(fixLength(emConstantValue.getPrice(), 7));
                        curConcernCode.setBp(emConstantValue.getPrice());
                    } else {
                        holdRemark.append(fixLength("", 7));
                    }
                } else {
                    holdRemark.append(fixLength("", 7));
                    holdRemark.append(fixLength("", 7));
                }

                String tmpType = yesterdayHigh ? "" + limitCodeMap.get(tsCode).getCount() : rangeOverLimit ? "R" : " ";
                BigDecimal tmpBar = BigDecimal.ZERO;
                if (rt.getVol() != null && checkOverLimit) {
                    tmpBar = deleteOrCalBar(rt.getTsCode(), rt.getTradeDate(), rt.getCurrentPri()).multiply(THOUSAND).setScale(0, RoundingMode.FLOOR);
                }
                if (threeLinked) {
                    logsMap.get(type).add(getPeekDesc(rt) + tmpType + " " + tsCode.substring(2, 6) + fixLength("", 1) + fixLength(rt.getPctChg(), 6) + fixLength(rt.getChangeHand(), 5) + holdRemark + fixLength(rt.getCurrentPri(), 6) + fixLength(rt.getVol() != null && checkOverLimit ? tmpBar : "", 8) + fixLength(rt.getCirculationMarketCap().divide(HUNDRED_MILLION, 3, RoundingMode.HALF_UP), 8) + fixLength(rt.getPe(), 8));
                    if (highLimit) {
                        todayBoardCodes.add(tsCode);
                        sendNewNotification(notification, rt, tsCode);
                        curConcernCode.setTableShow(true);
                    } else if (concerned || holds || yesterdayHigh || onboard) {
                        curConcernCode.setTableShow(true);
                    }

                }
                if (curConcernCode.getPe() != null
                    && curConcernCode.getPe().compareTo(Constants.PE_LIMIT) < 0
                    && curConcernCode.getPe().compareTo(BigDecimal.ZERO) > 0) {
                    curConcernCode.setTableShow(true);
                }
                curConcernCode.setTsCode(tsCode);
                if (!"R".equals(type)) {
                    if (!tmpType.trim().isBlank()) {
                        if ("R".equals(tmpType)) {
                            curConcernCode.setMark(type + " " + getPeekDesc(rt) + " " + tmpType);
                        } else {
                            curConcernCode.setMark(type + " " + tmpType + " " + getPeekDesc(rt));
                        }
                    } else {
                        curConcernCode.setMark(type + " " + getPeekDesc(rt));
                    }
                } else {
                    if (!tmpType.trim().isBlank()) {
                        curConcernCode.setMark(tmpType + " " + getPeekDesc(rt));
                    } else {
                        curConcernCode.setMark(getPeekDesc(rt));
                    }
                }
                curConcernCode.setRt(rt.getPctChg());
                curConcernCode.setH(rt.getChangeHand());
                curConcernCode.setCp(rt.getCurrentPri());
                curConcernCode.setBar(tmpBar);
                curConcernCode.setCm(rt.getCirculationMarketCap().divide(HUNDRED_MILLION, 3, RoundingMode.HALF_UP));
                curConcernCode.setPe(rt.getPe());
                curConcernCode.setTradeDate(curTradeDate);
                curConcernCodeList.add(curConcernCode);
            }
        }
        log.warn(StockUtils.concatChar("_", CHAR_LENGTH));
        String title = "|  I  |" + " T    CODE  |  " + fixLengthTitle(" RT ", 4) + fixLengthTitle(" H ", 3) + fixLengthTitle(" RR  ", 5) + fixLengthTitle(" BP ", 5) + fixLengthTitle(" CP ", 4) + fixLengthTitle("BAR", 6) + fixLengthTitle(" CM ", 6) + fixLengthTitle(" PE ", 6);
        log.warn(title.substring(0, title.length() - 2));
        log.warn(StockUtils.concatChar("‾", CHAR_LENGTH));
        printMapInfo(logsMap);
        log.warn(StockUtils.concatChar("‾", CHAR_LENGTH));
        if (!valueCodeCountMap.isEmpty() && refreshRangeOverCode) {
            List<RangeOverCodeValue> valueList = valueCodeCountMap.entrySet().stream().map(x -> new RangeOverCodeValue(x.getKey(), x.getValue())).toList();
            rangeOverCode.setCodeValue(valueList);
            rangeOverCodeService.saveEntity(rangeOverCode);
        }

        if (checkOverLimit && !todayBoardCodes.isEmpty()) {
            boardCode.setCodeValue(todayBoardCodes.stream().distinct().collect(Collectors.joining(",")));
            boardCodeService.saveEntity(boardCode);
        }

        if (nowMinute % 10 == 5 || nowMinute % 10 == 0
            || nowHour == 9 && nowMinute == 16
            || nowHour == 15 && nowMinute == 1) {
            CurCount curCount = statisticsCurCount(stockList);
            if (nowHour == 15 && nowMinute == 1) {
                BigDecimal totalAmount = BigDecimal.ZERO;
                BigDecimal zeroAmount = BigDecimal.ZERO;
                BigDecimal threeAmount = BigDecimal.ZERO;
                BigDecimal sixAmount = BigDecimal.ZERO;
                for (EmRealTimeStock stock : stockList) {
                    BigDecimal amount = Objects.requireNonNullElse(stock.getAmount(), BigDecimal.ZERO);
                    totalAmount = totalAmount.add(amount);
                    if (stock.getTsCode().charAt(0) == '0') {
                        zeroAmount = zeroAmount.add(amount);
                    } else if (stock.getTsCode().charAt(0) == '3') {
                        threeAmount = threeAmount.add(amount);
                    } else if (stock.getTsCode().charAt(0) == '6') {
                        sixAmount = sixAmount.add(amount);
                    }
                }
                curCount.setTotalAmount(totalAmount);
                curCount.setZeroAmount(zeroAmount);
                curCount.setThreeAmount(threeAmount);
                curCount.setSixAmount(sixAmount);
                curCount.setSummary(true);
            } else {
                curCount.setSummary(false);
            }
            curCountService.saveOne(curCount);
        }
        if (!curConcernCodeList.isEmpty()) {
            curConcernCodeService.saveList(curConcernCodeList);
            MySseEmitterUtil.sendMsgToClient(curConcernCodeService.findLast("1"), SSEMsgEnum.RT_CURRENT);
            sendRTMsg();
        }
    }

    private void sendRTMsg() {
        if (!MySseEmitterUtil.codeCacheEmpty()) {
            MySseEmitterUtil.codeSSECache.keySet().forEach(x -> MySseEmitterUtil.sendMsgToClient(prepareRTHisData(x), SSEMsgEnum.RT_HIS));
        }
    }

    private List<RealTimeVO> prepareRTHisData(String code) {
        return ArrayUtils.convertRealTimeVO(
                emRealTimeStockService.findRBarStockByCode(code), realBarService.findIntradayBar(code));
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
                if (em.getTsCode().startsWith("0")) {
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
                } else if (em.getTsCode().startsWith("3")) {
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
                } else if (em.getTsCode().startsWith("6")) {
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
        return new CurCount(stockList.getFirst().getTradeDate(), countArray);
    }

    private int convertTimeCount(String nowClock) {
        return Integer.parseInt(nowClock.substring(0, 2)) * 60 * 2 + Integer.parseInt(nowClock.substring(3, 5)) * 2 + (Integer.parseInt(nowClock.substring(6, 8)) >= 30 ? 1 : 0) - 1140;
    }

    private void printMapInfo(Map<String, List<String>> logsMap) {
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
                    log.warn("| " + fixLength(i + 1 + " ", 3) + x + " " + remarks.get(i));
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

    private String fixPositiveLength(Object str) {
        return String.format("%2s", str);
    }

    private boolean calRange(List<BigDecimal> values) {
//        BigDecimal maxValue = Collections.max(values);
        BigDecimal maxValue = values.getLast();
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
        return tmp >= 915 && tmp < 1132 || tmp >= 1300 && tmp < 1502;
    }

    private BigDecimal calRatio(BigDecimal curClosePri, BigDecimal doorPri) {
        return curClosePri.subtract(doorPri).multiply(HUNDRED).divide(doorPri, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal deleteOrCalBar(String tsCode, String tradeDate, BigDecimal curPri) {
        int dataCount = emRealTimeStockService.findRBarStockCountByCode(tsCode);
        int barCount = realBarService.findBarCount(tsCode);
        if (barCount > 0 && dataCount - barCount > 1) {
            realBarService.deleteBarByCode(tsCode);
        }
        return calBar(tsCode, tradeDate, curPri);
    }

    private BigDecimal calBar(String tsCode, String tradeDate, BigDecimal curPri) {
        RealBar newBar = new RealBar();
        RealBar lastBar = realBarService.findOne(tradeDate, tsCode);
        if (curPri == null || lastBar == null) {
            List<EmRealTimeStock> emRealTimeStockList = emRealTimeStockService.findRBarStockByCode(tsCode);
            if (emRealTimeStockList != null && !emRealTimeStockList.isEmpty()) {
                BigDecimal lastBarValue = BigDecimal.ZERO;
                EmRealTimeStock cur = emRealTimeStockList.getFirst();
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
