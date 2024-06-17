package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.RealBar;
import com.stock.pycurrent.repo.RealBarRepo;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fzc
 * @date 2024/1/5 14:54
 * @description
 */
@Service
@CommonsLog
public class RealBarService {
    @Resource
    private RealBarRepo realBarRepo;

    public RealBar findOne(String tradeDate, String tsCode) {
        return realBarRepo.findOne(tradeDate, tsCode);
    }

    public int findBarCount(String tsCode) {
        return realBarRepo.findBarCount(tsCode).intValue();
    }

    public void deleteBarByCode(String tsCode) {
        realBarRepo.deleteBarByCode(tsCode);
    }

    public Map<String, BigDecimal> findIntradayBar(String tsCode) {
        List<Object> tmpRes = realBarRepo.findIntradayBar(tsCode);
        Map<String, BigDecimal> res = new HashMap<>();
        if (tmpRes != null && !tmpRes.isEmpty()) {
            tmpRes.forEach(x -> {
                Object[] tmp = (Object[]) x;
                res.put(tmp[0].toString(), (BigDecimal) tmp[1]);
            });
        }
        return res;
    }

    public RealBar save(RealBar realBar) {
        return realBarRepo.saveAndFlush(realBar);
    }

}
