package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmDAStock;
import com.stock.pycurrent.entity.RocModel;
import com.stock.pycurrent.entity.emum.PyFuncEnum;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.repo.EmDAStockRepo;
import com.stock.pycurrent.repo.EmDNStockRepo;
import com.stock.pycurrent.repo.RocModelRepo;
import com.stock.pycurrent.util.DateUtils;
import com.stock.pycurrent.util.ExecutorUtils;
import com.stock.pycurrent.util.StockUtils;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CountDownLatch;

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


    public void initEMDailyData() {
        // 每日更新日行情
        String dailyNoStartDate = findMaxTradeDate(Constants.REHABILITATION_NO);
        String dailyAfterStartDate = findMaxTradeDate(Constants.REHABILITATION_AFTER);
        if (StockUtils.checkEmpty(dailyNoStartDate)) {
            dailyNoStartDate = Constants.EM_DEFAULT_START_DAY;
        }
        if (StockUtils.checkEmpty(dailyAfterStartDate)) {
            dailyAfterStartDate = Constants.EM_DEFAULT_START_DAY;
        }
        String endDate = StockUtils.getPullHourEndDate();
        log.info("ENTER-EM-INIT-DATA");
        if (DateUtils.before(dailyNoStartDate, endDate)) {
            StockUtils.initEmBasicData(dailyNoStartDate, endDate, Constants.REHABILITATION_NO, Constants.PERIOD_DAILY, PyFuncEnum.EM_HIS_NO_DAILY);
        }
        if (DateUtils.before(dailyAfterStartDate, endDate)) {
            StockUtils.initEmBasicData(dailyAfterStartDate, endDate, Constants.REHABILITATION_AFTER, Constants.PERIOD_DAILY, PyFuncEnum.EM_HIS_AFTER_DAILY);
        }
        log.info("EM-DAILY-OVER");
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


    @SneakyThrows
    public void initRocModel() {
        List<List<EmDAStock>> tmpDas = findEmCodeDataList();
        String start = DateUtils.getDateAtOffset(DateUtils.now(), 1, ChronoUnit.DAYS);
        List<RocModel> rocModelRes = createRocModel(start, tmpDas);
        rocModelRepo.saveAll(rocModelRes);
    }

    @SneakyThrows
    public List<RocModel> createRocModel(String params, List<List<EmDAStock>> allDatas) {
        if (rocModelRepo.checkExistParam(params) > 0) {
            return new ArrayList<>();
        }
        List<RocModel> res = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(allDatas.size());
        Date now = new Date();
        allDatas.forEach(x -> ExecutorUtils.addGuavaComplexTask(() -> {
            res.addAll(innerCreateRocModel(x, params, now));
            countDownLatch.countDown();
        }));
        countDownLatch.await();
        return res;
    }

    @SneakyThrows
    public List<RocModel> innerCreateRocModel(List<EmDAStock> datas, String params, Date now) {
        if (!datas.isEmpty()) {
            return StockUtils.findPeekPoint(datas, params, now);
        }
        return Collections.emptyList();
    }

    public List<List<EmDAStock>> findEmCodeDataList() {
        List<List<EmDAStock>> res = new ArrayList<>();
        List<String> codes = emDAStockRepo.findCodes();
        Collections.sort(codes);
        for (String x : codes) {
            if (StockUtils.availableCode(x)) {
                List<EmDAStock> emDAStocks = emDAStockRepo.findByCode(x);
                if (emDAStocks.stream().noneMatch(y -> y.getPriClose().compareTo(BigDecimal.ZERO) < 0)) {
                    emDAStocks.sort(Comparator.comparing(EmDAStock::getTradeDate));
                    res.add(emDAStocks);
                }
            }
        }
        return res;
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
}
