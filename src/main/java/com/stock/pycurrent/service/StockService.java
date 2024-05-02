package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmDAStock;
import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.LimitCode;
import com.stock.pycurrent.entity.RocModel;
import com.stock.pycurrent.entity.emum.PyFuncEnum;
import com.stock.pycurrent.entity.jsonvalue.LimitCodeValue;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.repo.EmDAStockRepo;
import com.stock.pycurrent.repo.EmDNStockRepo;
import com.stock.pycurrent.repo.LimitCodeRepo;
import com.stock.pycurrent.repo.RocModelRepo;
import com.stock.pycurrent.util.CalculateUtils;
import com.stock.pycurrent.util.DateUtils;
import com.stock.pycurrent.util.ExecutorUtils;
import com.stock.pycurrent.util.StockUtils;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author fzc
 * @date 2024/2/20 13:40
 * @description
 */
@Component
@CommonsLog
public class StockService {

    private EmDAStockRepo emDAStockRepo;
    private EmDNStockRepo emDNStockRepo;

    private RocModelRepo rocModelRepo;

    private LimitCodeRepo limitCodeRepo;


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
        LimitCode nowDayOne = limitCodeRepo.findByDate(nowDay);
        if (nowDayOne != null && nowDayOne.getTradeDate() != null) {
            return;
        }
        List<EmDNStock> emDNStockList = emDNStockRepo.findCurrent(nowDay);
        if (emDNStockList == null || emDNStockList.isEmpty()) {
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
        newLimitCodeOne.setCodeValue(newLimitCodeValues);
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

    @Autowired
    public void setEmDAStockRepo(EmDAStockRepo emDAStockRepo) {
        this.emDAStockRepo = emDAStockRepo;
    }

    @Autowired
    public void setEmDNStockRepo(EmDNStockRepo emDNStockRepo) {
        this.emDNStockRepo = emDNStockRepo;
    }

    @Autowired
    public void setRocModelRepo(RocModelRepo rocModelRepo) {
        this.rocModelRepo = rocModelRepo;
    }

    @Autowired
    public void setLimitCodeRepo(LimitCodeRepo limitCodeRepo) {
        this.limitCodeRepo = limitCodeRepo;
    }
}
