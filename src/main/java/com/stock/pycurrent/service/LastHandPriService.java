package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmDAStock;
import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.LastHandPri;
import com.stock.pycurrent.repo.EmDAStockRepo;
import com.stock.pycurrent.repo.EmDNStockRepo;
import com.stock.pycurrent.repo.LastHandPriRepo;
import com.stock.pycurrent.util.DateUtils;
import com.stock.pycurrent.util.ExecutorUtils;
import com.stock.pycurrent.util.StockUtils;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

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
    private EmDAStockRepo emDAStockRepo;
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
                log.warn(DateUtils.commonNow() + " ,lastHandPris:" + lastHandPris.size() + " ,code:" + x.getTsCode());
                LastHandPri lastHandPri = convertLastHandPri(emDNStockRepo.findLastHandPri(x.getTsCode(), now));
                if (lastHandPri != null) {
                    lastHandPris.add(lastHandPri);
                }
            });
            lastHandPriRepo.saveAllAndFlush(lastHandPris);
        }
    }

    private LastHandPri convertLastHandPri(List<Object[]> queryResList) {
        Object[] queryRes = queryResList.getFirst();
        if (queryRes[0] == null) {
            return null;
        }
        LastHandPri lastHandPri = new LastHandPri();
        lastHandPri.setTradeDate(queryRes[0].toString());
        lastHandPri.setTsCode(queryRes[1].toString());
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


    public void createRecode() {
        List<EmDNStock> emDNStockList = emDNStockRepo.findCurrent(DateUtils.now());
        for (EmDNStock emDNStock : emDNStockList) {
            ExecutorUtils.addGuavaComplexTask(() -> {
                creatLastHandPri(emDNStock.getTsCode());
            });
        }
    }

    public void creatLastHandPri(String tsCode) {
        String minDate = emDNStockRepo.findMinTradeDate(tsCode);
        List<EmDNStock> emDNStockList = emDNStockRepo.findOverDate(minDate, tsCode);
        if (emDNStockList != null && !emDNStockList.isEmpty()) {
            emDNStockList = emDNStockList.stream().sorted(Comparator.comparing(EmDNStock::getTradeDate)).toList();
            List<String> tradeDates = emDNStockList.stream().map(EmDNStock::getTradeDate).toList();
            Map<String, BigDecimal> handMap = new HashMap<>();
            Map<String, BigDecimal> priceMap = new HashMap<>();
            BigDecimal five = BigDecimal.valueOf(5);
            BigDecimal ten = BigDecimal.valueOf(10);
            BigDecimal twenty = BigDecimal.valueOf(20);
            BigDecimal thirty = BigDecimal.valueOf(30);
            BigDecimal fifty = BigDecimal.valueOf(50);
            BigDecimal hundred = BigDecimal.valueOf(100);
            for (EmDNStock emDNStock : emDNStockList) {
                handMap.put(emDNStock.getTradeDate(), emDNStock.getChangeHand());
                priceMap.put(emDNStock.getTradeDate(), emDNStock.getAmount().divide(BigDecimal.valueOf(emDNStock.getVol()).multiply(hundred), 3, RoundingMode.HALF_UP));
            }
            int si = tradeDates.indexOf("20240102");
            List<LastHandPri> res = new ArrayList<>();
            for (int j = si; j < tradeDates.size(); j++) {
                String td = tradeDates.get(j);
                BigDecimal totalHand = handMap.get(td);
                BigDecimal avgPri = priceMap.get(td);
                LastHandPri lastHandPri = new LastHandPri();
                lastHandPri.setTsCode(tsCode);
                lastHandPri.setTradeDate(td);
                if (totalHand.compareTo(five) >= 0) {
                    lastHandPri.setLastFivePri(priceMap.get(td));
                }
                if (totalHand.compareTo(ten) >= 0) {
                    lastHandPri.setLastTenPri(priceMap.get(td));
                }
                if (totalHand.compareTo(twenty) >= 0) {
                    lastHandPri.setLastTwentyPri(priceMap.get(td));
                }
                if (totalHand.compareTo(thirty) >= 0) {
                    lastHandPri.setLastThirtyPri(priceMap.get(td));
                }
                if (totalHand.compareTo(fifty) >= 0) {
                    lastHandPri.setLastFiftyPri(priceMap.get(td));
                }
                for (int i = j - 1; i >= 0; i--) {
                    String tdtmp = tradeDates.get(i);
                    BigDecimal tavgPri = avgPri.multiply(totalHand).add(handMap.get(tdtmp).multiply(priceMap.get(tdtmp))).divide(totalHand.add(handMap.get(tdtmp)), 3, RoundingMode.HALF_UP);
                    BigDecimal ttotalHand = totalHand.add(handMap.get(tradeDates.get(i)));
                    if (ttotalHand.compareTo(five) >= 0 && lastHandPri.getLastFivePri() == null) {
                        lastHandPri.setLastFivePri(StockUtils.calVPrice(totalHand, ttotalHand, avgPri, tavgPri, five));
                    }
                    if (ttotalHand.compareTo(ten) >= 0 && lastHandPri.getLastTenPri() == null) {
                        lastHandPri.setLastTenPri(StockUtils.calVPrice(totalHand, ttotalHand, avgPri, tavgPri, ten));
                    }
                    if (ttotalHand.compareTo(twenty) >= 0 && lastHandPri.getLastTwentyPri() == null) {
                        lastHandPri.setLastTwentyPri(StockUtils.calVPrice(totalHand, ttotalHand, avgPri, tavgPri, twenty));
                    }
                    if (ttotalHand.compareTo(thirty) >= 0 && lastHandPri.getLastThirtyPri() == null) {
                        lastHandPri.setLastThirtyPri(StockUtils.calVPrice(totalHand, ttotalHand, avgPri, tavgPri, thirty));
                    }
                    if (ttotalHand.compareTo(fifty) >= 0 && lastHandPri.getLastFiftyPri() == null) {
                        lastHandPri.setLastFiftyPri(StockUtils.calVPrice(totalHand, ttotalHand, avgPri, tavgPri, fifty));
                    }
                    if (ttotalHand.compareTo(hundred) >= 0 && lastHandPri.getLastHundredPri() == null) {
                        lastHandPri.setLastHundredPri(StockUtils.calVPrice(totalHand, ttotalHand, avgPri, tavgPri, hundred));
                        res.add(lastHandPri);
                        break;
                    }
                    totalHand = ttotalHand;
                    avgPri = tavgPri;
                }
            }
            if (!res.isEmpty()) {
                lastHandPriRepo.saveAllAndFlush(res);
            }
        }
    }
}
