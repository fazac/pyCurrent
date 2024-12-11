package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.*;
import com.stock.pycurrent.entity.emum.PyFuncEnum;
import com.stock.pycurrent.entity.jsonvalue.LimitCodeValue;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.repo.*;
import com.stock.pycurrent.util.CalculateUtils;
import com.stock.pycurrent.util.DateUtils;
import com.stock.pycurrent.util.ExecutorUtils;
import com.stock.pycurrent.util.StockUtils;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author fzc
 * @date 2024/2/20 13:40
 * @description
 */
@Component
@CommonsLog
public class StockService {
    @Resource
    private EmDAStockRepo emDAStockRepo;
    @Resource
    private EmDNStockRepo emDNStockRepo;
    @Resource
    private RocModelRepo rocModelRepo;
    @Resource
    private LimitCodeRepo limitCodeRepo;
    @Resource
    private StockCalModelRepo stockCalModelRepo;

    public void initEMDailyData() {
        // 每日更新日行情
        log.warn("EM-INIT-GETDATA-ENTER");
        String dailyNoStartDate = findMaxTradeDate(Constants.REHABILITATION_NO);
        String dailyAfterStartDate = findMaxTradeDate(Constants.REHABILITATION_AFTER);
        if (StockUtils.checkEmpty(dailyNoStartDate)) {
            dailyNoStartDate = Constants.EM_DEFAULT_START_DAY;
        } else {
            while (true) {
                long dnCount = findCountTradeDate(Constants.REHABILITATION_NO, dailyNoStartDate);
                if (dnCount >= 5250) {
                    break;
                } else {
                    deleteByTradeDate(Constants.REHABILITATION_NO, dailyNoStartDate);
                    dailyNoStartDate = findMaxTradeDate(Constants.REHABILITATION_NO);
                }
            }
        }

        if (StockUtils.checkEmpty(dailyAfterStartDate)) {
            dailyAfterStartDate = Constants.EM_DEFAULT_START_DAY;
        } else {
            while (true) {
                long daCount = findCountTradeDate(Constants.REHABILITATION_AFTER, dailyAfterStartDate);
                if (daCount > 5250) {
                    break;
                } else {
                    deleteByTradeDate(Constants.REHABILITATION_AFTER, dailyAfterStartDate);
                    dailyAfterStartDate = findMaxTradeDate(Constants.REHABILITATION_AFTER);
                }
            }
        }
        log.warn("EM-INIT-GETDATA-OVER");
        String endDate = StockUtils.getPullHourEndDate();

        log.warn("ENTER-EM-INIT-DATA");
        if (DateUtils.before(dailyNoStartDate, endDate)) {
            StockUtils.initEmBasicData(dailyNoStartDate, endDate, Constants.REHABILITATION_NO, Constants.PERIOD_DAILY, PyFuncEnum.EM_HIS_NO_DAILY);
        }
        log.warn("OVER-EM-INIT-DATA-1");
        if (DateUtils.before(dailyAfterStartDate, endDate)) {
            StockUtils.initEmBasicData(dailyAfterStartDate, endDate, Constants.REHABILITATION_AFTER, Constants.PERIOD_DAILY, PyFuncEnum.EM_HIS_AFTER_DAILY);
        }
        log.warn("OVER-EM-INIT-DATA-2");
        log.warn("EM-DAILY-OVER");
    }

    public String findMaxTradeDate(String period) {
        String lastDate = "";
        if (Constants.REHABILITATION_NO.equals(period)) {
            lastDate = emDNStockRepo.findMaxTradeDate();
        } else if (Constants.REHABILITATION_AFTER.equals(period)) {
            lastDate = emDAStockRepo.findMaxTradeDate();
        }
        return StockUtils.checkEmpty(lastDate) ? "" : lastDate;
    }

    public Long findCountTradeDate(String period, String date) {
        Long count = 0L;
        if (Constants.REHABILITATION_NO.equals(period)) {
            count = emDNStockRepo.findCountByDate(date);
        } else if (Constants.REHABILITATION_AFTER.equals(period)) {
            count = emDAStockRepo.findCountByDate(date);
        }
        return count;
    }

    public void deleteByTradeDate(String period, String date) {
        if (Constants.REHABILITATION_NO.equals(period)) {
            emDNStockRepo.deleteByDate(date);
        } else if (Constants.REHABILITATION_AFTER.equals(period)) {
            emDAStockRepo.deleteByDate(date);
        }
    }


    @SneakyThrows
    public void initRocModel() {
        String start = LocalDateTime.now().getHour() < 16 ? DateUtils.now() : DateUtils.getDateAtOffset(DateUtils.now(), 1, ChronoUnit.DAYS);
        if (rocModelRepo.checkExistParam(start) > 0) {
            return;
        }
        List<String> codes = new ArrayList<>(emDAStockRepo.findCodes().stream().filter(StockUtils::availableCode).toList());
        Collections.sort(codes);
        Date now = new Date();
        codes.forEach(x -> ExecutorUtils.addGuavaComplexTask(() -> rocModelRepo.saveAll(innerCreateRocModel(emDAStockRepo.findByCode(x), start, now))));
    }

    @SneakyThrows
    public List<RocModel> innerCreateRocModel(List<EmDAStock> datas, String params, Date now) {
        if (!datas.isEmpty()) {
            return StockUtils.findPeekPoint(datas, params, now);
        }
        return Collections.emptyList();
    }

    public void createLimitCode() {
        String nowDay = DateUtils.now();
        String lastDate = limitCodeRepo.findMaxDate();
        if (nowDay.equals(lastDate)) {
            return;
        }
        List<EmDNStock> emDNStockList = null;
        boolean flag = false;
        while (true) {
            LimitCode nowDayOne = limitCodeRepo.findByDate(nowDay);
            if (nowDayOne != null && nowDayOne.getTradeDate() != null) {
                flag = true;
                break;
            } else {
                emDNStockList = emDNStockRepo.findCurrent(nowDay);
                if (emDNStockList == null || emDNStockList.isEmpty()) {
                    nowDay = DateUtils.getDateAtOffset(nowDay, -1, ChronoUnit.DAYS);
                } else {
                    break;
                }
            }
        }
        if (flag) {
            return;
        }
        emDNStockList.sort(Comparator.comparing(EmDNStock::getTsCode));
        LimitCode newLimitCodeOne = new LimitCode();
        newLimitCodeOne.setTradeDate(nowDay);
        List<LimitCodeValue> newLimitCodeValues = new ArrayList<>();
        for (EmDNStock rt : emDNStockList) {
            String tsCode = rt.getTsCode();
            if ((tsCode.startsWith("0") || tsCode.startsWith("60")) && rt.getPctChg().compareTo(Constants.EIGHT) > 0
                    || tsCode.startsWith("3") && rt.getPctChg().compareTo(Constants.EIGHTEEN) > 0) {
                int i = checkReachLimit(rt, 0);
                if (i > 0) {
                    LimitCodeValue limitCodeValue = new LimitCodeValue();
                    limitCodeValue.setCode(tsCode);
                    limitCodeValue.setCount(i);
                    newLimitCodeValues.add(limitCodeValue);
                }
            }
        }
        newLimitCodeOne.setCodeValue(newLimitCodeValues.stream().distinct().collect(Collectors.toList()));
        limitCodeRepo.saveAndFlush(newLimitCodeOne);
    }

    private int checkReachLimit(EmDNStock emDNStock, int i) {
        EmDNStock leftOne = emDNStockRepo.findLeftOne(emDNStock.getTsCode(), emDNStock.getTradeDate());
        if (leftOne != null) {
            String tsCode = emDNStock.getTsCode();
            if ((tsCode.startsWith("0") || tsCode.startsWith("60")) && CalculateUtils.reachTenLimit(emDNStock, leftOne) || tsCode.startsWith("3") && CalculateUtils.reachTwentyLimit(emDNStock, leftOne)) {
                i++;
                return checkReachLimit(leftOne, i);
            }
        }
        return i;
    }

    public void generateModel() {
        String code = "300171";
        List<EmDAStock> emDAStocks = emDAStockRepo.findByCode(code);
        emDAStocks.forEach(x -> {
            List<StockCalModel> stockCalModels = stockCalModelRepo.findLastByCode(code);
            if (stockCalModels == null || stockCalModels.isEmpty()) {
                StockCalModel stockCalModel = convertDA2Model(x);
                stockCalModel.setLevel(1);
                stockCalModelRepo.saveAndFlush(stockCalModel);
            } else {
                stockCalModelRepo.saveAllAndFlush(StockUtils.generateCalModel(convertDA2Model(x), stockCalModels));
            }
        });
    }

    private StockCalModel convertDA2Model(EmDAStock emDAStock) {
        StockCalModel stockCalModel = new StockCalModel();
        stockCalModel.setTsCode(emDAStock.getTsCode());
        stockCalModel.setTradeDate(emDAStock.getTradeDate());
        stockCalModel.setType(1);
        stockCalModel.setPrice(emDAStock.getPriClose());
        return stockCalModel;
    }
}
