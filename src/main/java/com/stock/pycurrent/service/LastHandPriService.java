package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.LastHandPri;
import com.stock.pycurrent.repo.EmDNStockRepo;
import com.stock.pycurrent.repo.LastHandPriRepo;
import com.stock.pycurrent.util.DateUtils;
import com.stock.pycurrent.util.StockUtils;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fzc
 * @date 2024/7/8 9:36
 * @description
 */
@Service
@CommonsLog
public class LastHandPriService {
    @Resource
    private LastHandPriRepo lastHandPriRepo;
    @Resource
    private EmDNStockRepo emDNStockRepo;
    @Resource
    private EmRealTimeStockService emRealTimeStockService;

    public void createIntradayLHP() {
        String now = DateUtils.now();
        String maxDate = lastHandPriRepo.findMaxDate();
        if (now.equals(maxDate) || !StockUtils.afterPullHour()) {
            return;
        }
        List<EmDNStock> emDNStockList = emDNStockRepo.findCurrent(DateUtils.now());
        createLHP(now, emDNStockList);
    }

    public void createPastRecord() {
        List<String> tradeDates = emDNStockRepo.findIntraYearDate();
        for (String now : tradeDates) {
            List<EmDNStock> emDNStockList = emDNStockRepo.findCurrent(now);
            createLHP(now, emDNStockList);
        }
    }

    private void createLHP(String now, List<EmDNStock> emDNStockList) {
        if (emDNStockList != null && !emDNStockList.isEmpty()) {
            List<LastHandPri> lastHandPris = new ArrayList<>();
            emDNStockList.stream().filter(x -> x.getVol() != null && x.getVol() > 0).forEach(x ->
            {
                log.warn(DateUtils.commonNow() + " ,lastHandPris:" + lastHandPris.size());
                lastHandPris.add(convertLastHandPri(emRealTimeStockService.findLastHandPri(x.getTsCode(), now)));
            });
            lastHandPriRepo.saveAllAndFlush(lastHandPris);
        }
    }

    private LastHandPri convertLastHandPri(List<Object[]> queryResList) {
        Object[] queryRes = queryResList.getFirst();
        LastHandPri lastHandPri = new LastHandPri();
        lastHandPri.setTradeDate(queryRes[0].toString());
        lastHandPri.setTsCode(queryRes[1].toString());
        lastHandPri.setCurrentPri(new BigDecimal(queryRes[2].toString()));
        lastHandPri.setLastFivePri(new BigDecimal(queryRes[3].toString()));
        lastHandPri.setLastTenPri(new BigDecimal(queryRes[4].toString()));
        lastHandPri.setLastTwentyPri(new BigDecimal(queryRes[5].toString()));
        lastHandPri.setLastThirtyPri(new BigDecimal(queryRes[6].toString()));
        lastHandPri.setLastFiftyPri(new BigDecimal(queryRes[7].toString()));
        lastHandPri.setLastHundredPri(new BigDecimal(queryRes[8].toString()));
        return lastHandPri;
    }

    public List<LastHandPri> findLHPByCode(String code) {
        return lastHandPriRepo.findLHPByCode(code);
    }
}
