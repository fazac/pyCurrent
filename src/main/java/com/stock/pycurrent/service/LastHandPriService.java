package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmDAStock;
import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.LastHandPri;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.repo.EmDAStockRepo;
import com.stock.pycurrent.repo.EmDNStockRepo;
import com.stock.pycurrent.repo.LastHandPriRepo;
import com.stock.pycurrent.util.DateUtils;
import com.stock.pycurrent.util.ExecutorUtils;
import com.stock.pycurrent.util.StockUtils;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    private EmDAStockRepo emDAStockRepo;
    @Resource
    private EmRealTimeStockService emRealTimeStockService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void createIntradayLHP() {
        String now = DateUtils.now();
        String maxDate = lastHandPriRepo.findMaxDate(1);
        if (now.equals(maxDate) || !StockUtils.afterPullHour()) {
            return;
        }
        List<String> handleDates = emDNStockRepo.findByDateInterval(maxDate, now);
        if (handleDates != null && !handleDates.isEmpty()) {
            handleDates.forEach(x -> {
                List<EmDNStock> emDNStockList = emDNStockRepo.findCurrent(x);
                createLHP(x, emDNStockList);
            });
        }
    }

    public void createDAIntradayLHP() {
        String now = DateUtils.now();
        String maxDate = lastHandPriRepo.findMaxDate(2);
        if (now.equals(maxDate) || !StockUtils.afterPullHour()) {
            return;
        }
        List<String> handleDates = emDAStockRepo.findByDateInterval(maxDate, now);
        if (handleDates != null && !handleDates.isEmpty()) {
            handleDates.forEach(x -> {
                List<EmDAStock> emDAStockList = emDAStockRepo.findCurrent(x);
                createDALHP(x, emDAStockList);
            });
        }
    }

    private void createDALHP(String now, List<EmDAStock> emDAStockList) {
        if (emDAStockList != null && !emDAStockList.isEmpty()) {
            List<LastHandPri> lastHandPris = new ArrayList<>();
            emDAStockList.stream().filter(x -> x.getVol() != null && x.getVol() > 0).forEach(x ->
            {
//                log.warn(DateUtils.commonNow() + " ,lastHandPris:" + lastHandPris.size() + " ,code:" + x.getTsCode());
                LastHandPri lastHandPri = convertLastHandPri(emDAStockRepo.findLastHandPri(x.getTsCode(), now), 2);
                if (lastHandPri != null) {
                    lastHandPris.add(lastHandPri);
                }
            });
            lastHandPriRepo.saveAllAndFlush(lastHandPris);
        }
    }


    public void createPastRecord() {
        List<String> tradeDates = emDNStockRepo.findIntraYearDate();
        for (String now : tradeDates) {
            List<EmDNStock> emDNStockList = emDNStockRepo.findCurrent(now);
            createLHP(now, emDNStockList);
        }
    }

    @Deprecated
    public void createPastAllRecord() {
        List<String> tradeDates = emDNStockRepo.findAllDate();
        tradeDates.forEach(now -> {
            ExecutorUtils.addGuavaComplexTask(() -> {
                List<EmDNStock> emDNStockList = emDNStockRepo.findCurrent(now);
                createLHP(now, emDNStockList);
                List<EmDAStock> emDAStockList = emDAStockRepo.findCurrent(now);
                createDALHP(now, emDAStockList);
            });
        });

//        for (String now : tradeDates) {
//            List<EmDNStock> emDNStockList = emDNStockRepo.findCurrent(now);
//            createLHP(now, emDNStockList);
//            List<EmDAStock> emDAStockList = emDAStockRepo.findCurrent(now);
//            createDALHP(now, emDAStockList);
//        }
    }


    private BigDecimal calAvgPrice(BigDecimal amount, Long vol) {
        return amount.divide(BigDecimal.valueOf(vol).multiply(Constants.HUNDRED), 3, RoundingMode.HALF_UP);
    }

    private BigDecimal calAvgLevelPrice(BigDecimal leftAmount, BigDecimal leftHand, Long leftVol,
                                        BigDecimal rightAmount, BigDecimal rightHand, Long rightVol,
                                        BigDecimal level) {
        BigDecimal rightPrice = calAvgPrice(rightAmount, rightVol);
        if (leftVol == 0) {
            return rightPrice;
        }
        BigDecimal leftPrice = calAvgPrice(leftAmount, leftVol);
        return rightPrice.subtract(leftPrice).divide(rightHand.subtract(leftHand), 3, RoundingMode.HALF_UP)
                .multiply(level.subtract(leftHand)).add(leftPrice);
    }

    @SneakyThrows
    public void createPastAllRecordWithRedis() {
//        List<String> codes = emDNStockRepo.findAllCode();
////        List<String> codes = Arrays.asList("300442", "300973", "300170");
//        codes.forEach(x -> {
//            ExecutorUtils.addCsComplexTask(() -> {
//                List<LastHandPri> lastHandPris = new ArrayList<>();
//                List<EmDNStock> emDNStockList = emDNStockRepo.findByCode(x);
//                if (emDNStockList != null && !emDNStockList.isEmpty()) {
//                    emDNStockList.forEach(y -> {
//                        redisTemplate.opsForList().rightPush(x, y);
//                    });
//                }
//                Long size = redisTemplate.opsForList().size(x);
//                while (size != null && size > 0) {
//                    lastHandPris.add(createLHPWithRedis(x));
//                    redisTemplate.opsForList().leftPop(x);
//                    size = redisTemplate.opsForList().size(x);
//                }
//                lastHandPriRepo.saveAll(lastHandPris);
//                return null;
//            });
//        });
//        for (int i = 0; i < codes.size(); i++) {
//            ExecutorUtils.COMPLETION_SERVICE.take().get();
//        }
//        log.info("ALL DN OVER");

        List<String> daCodes = emDAStockRepo.findAllCode();
//        List<String> daCodes = Arrays.asList("300442", "300973", "300170");
        daCodes.forEach(x -> {
            ExecutorUtils.addCsComplexTask(() -> {
                List<LastHandPri> lastHandPris = new ArrayList<>();
                List<EmDAStock> emDAStockList = emDAStockRepo.findByCodeDesc(x);
                if (emDAStockList != null && !emDAStockList.isEmpty()) {
                    emDAStockList.forEach(y -> {
                        redisTemplate.opsForList().rightPush(x, y);
                    });
                }
                Long size = redisTemplate.opsForList().size(x);
                while (size != null && size > 0) {
                    lastHandPris.add(createLHPDAWithRedis(x));
                    redisTemplate.opsForList().leftPop(x);
                    size = redisTemplate.opsForList().size(x);
                }
                lastHandPriRepo.saveAll(lastHandPris);
                return null;
            });
        });
        for (int i = 0; i < daCodes.size(); i++) {
            ExecutorUtils.COMPLETION_SERVICE.take().get();
        }
        log.info("ALL DA OVER");
    }

    private LastHandPri createLHPWithRedis(String code) {
        Long length = redisTemplate.opsForList().size(code);
        BigDecimal totalHand = BigDecimal.ZERO;
        BigDecimal beforeTotalHand;
        Long totalVol = 0L;
        Long beforeTotalVol;
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal beforeTotalAmount;
        LastHandPri lastHandPri = new LastHandPri();
        if (length != null) {
            for (int i = 0; i < length; i++) {
                beforeTotalHand = totalHand;
                beforeTotalVol = totalVol;
                beforeTotalAmount = totalAmount;
                EmDNStock current = (EmDNStock) Objects.requireNonNull(redisTemplate.opsForList().index(code, i));
                totalHand = totalHand.add(current.getChangeHand());
                totalAmount = totalAmount.add(current.getAmount());
                totalVol += current.getVol();
                if (setLastHandPriValue(totalHand, beforeTotalHand, totalVol, beforeTotalVol, totalAmount, beforeTotalAmount, lastHandPri))
                    break;
            }
        }
        lastHandPri.setTsCode(code);
        lastHandPri.setType(1);
        lastHandPri.setTradeDate(((EmDNStock) Objects.requireNonNull(redisTemplate.opsForList().index(code, 0))).getTradeDate());
        return lastHandPri;
    }

    private boolean setLastHandPriValue(BigDecimal totalHand, BigDecimal beforeTotalHand, Long totalVol, Long beforeTotalVol, BigDecimal totalAmount, BigDecimal beforeTotalAmount, LastHandPri lastHandPri) {
        if (totalHand.compareTo(Constants.FIVE) == 0) {
            lastHandPri.setLastFivePri(calAvgPrice(totalAmount, totalVol));
        } else if (totalHand.compareTo(Constants.FIVE) > 0 && beforeTotalHand.compareTo(Constants.FIVE) < 0) {
            lastHandPri.setLastFivePri(calAvgLevelPrice(beforeTotalAmount, beforeTotalHand, beforeTotalVol, totalAmount, totalHand, totalVol, Constants.FIVE));
        }
        if (totalHand.compareTo(Constants.TEN) == 0) {
            lastHandPri.setLastTenPri(calAvgPrice(totalAmount, totalVol));
        } else if (totalHand.compareTo(Constants.TEN) > 0 && beforeTotalHand.compareTo(Constants.TEN) < 0) {
            lastHandPri.setLastTenPri(calAvgLevelPrice(beforeTotalAmount, beforeTotalHand, beforeTotalVol, totalAmount, totalHand, totalVol, Constants.TEN));
        }
        if (totalHand.compareTo(Constants.TWENTY) == 0) {
            lastHandPri.setLastTwentyPri(calAvgPrice(totalAmount, totalVol));
        } else if (totalHand.compareTo(Constants.TWENTY) > 0 && beforeTotalHand.compareTo(Constants.TWENTY) < 0) {
            lastHandPri.setLastTwentyPri(calAvgLevelPrice(beforeTotalAmount, beforeTotalHand, beforeTotalVol, totalAmount, totalHand, totalVol, Constants.TWENTY));
        }
        if (totalHand.compareTo(Constants.THIRTY) == 0) {
            lastHandPri.setLastThirtyPri(calAvgPrice(totalAmount, totalVol));
        } else if (totalHand.compareTo(Constants.THIRTY) > 0 && beforeTotalHand.compareTo(Constants.THIRTY) < 0) {
            lastHandPri.setLastThirtyPri(calAvgLevelPrice(beforeTotalAmount, beforeTotalHand, beforeTotalVol, totalAmount, totalHand, totalVol, Constants.THIRTY));
        }
        if (totalHand.compareTo(Constants.FIFTY) == 0) {
            lastHandPri.setLastFiftyPri(calAvgPrice(totalAmount, totalVol));
        } else if (totalHand.compareTo(Constants.FIFTY) > 0 && beforeTotalHand.compareTo(Constants.FIFTY) < 0) {
            lastHandPri.setLastFiftyPri(calAvgLevelPrice(beforeTotalAmount, beforeTotalHand, beforeTotalVol, totalAmount, totalHand, totalVol, Constants.FIFTY));
        }
        if (totalHand.compareTo(Constants.HUNDRED) == 0) {
            lastHandPri.setLastHundredPri(calAvgPrice(totalAmount, totalVol));
            return true;
        } else if (totalHand.compareTo(Constants.HUNDRED) > 0 && beforeTotalHand.compareTo(Constants.HUNDRED) < 0) {
            lastHandPri.setLastHundredPri(calAvgLevelPrice(beforeTotalAmount, beforeTotalHand, beforeTotalVol, totalAmount, totalHand, totalVol, Constants.HUNDRED));
            return true;
        }
        return false;
    }

    private LastHandPri createLHPDAWithRedis(String code) {
        Long length = redisTemplate.opsForList().size(code);
        BigDecimal totalHand = BigDecimal.ZERO;
        BigDecimal beforeTotalHand;
        Long totalVol = 0L;
        Long beforeTotalVol;
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal beforeTotalAmount;
        LastHandPri lastHandPri = new LastHandPri();
        if (length != null) {
            for (int i = 0; i < length; i++) {
                beforeTotalHand = totalHand;
                beforeTotalVol = totalVol;
                beforeTotalAmount = totalAmount;
                EmDAStock current = (EmDAStock) Objects.requireNonNull(redisTemplate.opsForList().index(code, i));
                totalVol += current.getVol();
                totalHand = totalHand.add(current.getChangeHand());
                totalAmount = totalAmount.add(current.getAmount());
                if (setLastHandPriValue(totalHand, beforeTotalHand, totalVol, beforeTotalVol, totalAmount, beforeTotalAmount, lastHandPri))
                    break;
            }
        }
        lastHandPri.setTsCode(code);
        lastHandPri.setType(2);
        lastHandPri.setTradeDate(((EmDAStock) Objects.requireNonNull(redisTemplate.opsForList().index(code, 0))).getTradeDate());
        return lastHandPri;
    }


    private void createLHP(String now, List<EmDNStock> emDNStockList) {
        if (emDNStockList != null && !emDNStockList.isEmpty()) {
            List<LastHandPri> lastHandPris = new ArrayList<>();
            emDNStockList.stream().filter(x -> x.getVol() != null && x.getVol() > 0).forEach(x ->
            {
                LastHandPri lastHandPri = convertLastHandPri(emDNStockRepo.findLastHandPri(x.getTsCode(), now), 1);
                if (lastHandPri != null) {
                    lastHandPris.add(lastHandPri);
                }
            });
            lastHandPriRepo.saveAllAndFlush(lastHandPris);
        }
    }

    private LastHandPri convertLastHandPri(List<Object[]> queryResList, int type) {
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
        lastHandPri.setType(type);
        return lastHandPri;
    }

    public List<LastHandPri> findLHPByCode(String code, int type) {
        return lastHandPriRepo.findLHPByCode(code, 1);
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
