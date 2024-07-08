package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.LastHandPri;
import com.stock.pycurrent.repo.EmDNStockRepo;
import com.stock.pycurrent.repo.LastHandPriRepo;
import com.stock.pycurrent.util.DateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author fzc
 * @date 2024/7/8 9:36
 * @description
 */
@Service
public class LastHandPriService {
    @Resource
    private LastHandPriRepo lastHandPriRepo;
    @Resource
    private EmDNStockRepo emDNStockRepo;

    public void createIntradayLHP() {
        String now = DateUtils.now();
        String maxDate = lastHandPriRepo.findMaxDate();
        if (now.equals(maxDate)) {
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
            emDNStockList.stream().filter(x -> x.getVol() != null && x.getVol() > 0).forEach(x -> lastHandPris.add(emDNStockRepo.findLastHandPri(x.getTsCode(), now)));
            lastHandPriRepo.saveAllAndFlush(lastHandPris);
        }
    }

    public List<LastHandPri> findLHPByCode(String code) {
        return lastHandPriRepo.findLHPByCode(code);
    }
}
