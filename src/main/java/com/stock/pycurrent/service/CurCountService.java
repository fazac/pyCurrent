package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.CurCount;
import com.stock.pycurrent.repo.CurCountRepo;
import jakarta.annotation.Resource;
import jdk.jshell.EvalException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author fzc
 * @date 2024/1/29 10:52
 * @description
 */
@Service
public class CurCountService {
    @Resource
    private CurCountRepo curCountRepo;

    public void saveOne(CurCount curCount) {
        curCountRepo.saveAndFlush(curCount);
    }

    public List<CurCount> findLastAll() {
        List<CurCount> curCounts = curCountRepo.findLastAll();
        List<CurCount> preCounts = curCountRepo.findYesterdayAll();
        if (preCounts != null && !preCounts.isEmpty()) {
            Map<String, CurCount> lastMap = preCounts.stream().collect(Collectors.toMap(x -> x.getTradeDate().substring(11, 19), Function.identity()));
            curCounts.forEach(x -> {
                CurCount tmp = lastMap.getOrDefault(x.getTradeDate().substring(11, 19), null);
                if (tmp != null) {
                    x.setPreTotal(tmp.getTotalAmount());
                    x.setPreThree(tmp.getThreeAmount());
                    x.setPreSix(tmp.getSixAmount());
                    x.setPreZero(tmp.getZeroAmount());
                }
            });
        }
        return curCounts;
    }

    public List<CurCount> findSummaryList() {
        return curCountRepo.findSummaryList();
    }

}
