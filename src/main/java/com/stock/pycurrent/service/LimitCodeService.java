package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.LimitCode;
import com.stock.pycurrent.repo.LimitCodeRepo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author fzc
 * @date 2024/1/16 11:04
 * @description
 */

@Service
public class LimitCodeService {
    @Resource
    private LimitCodeRepo limitCodeRepo;

    @SuppressWarnings("unused")
    public LimitCode findByDate(String tradeDate) {
        return limitCodeRepo.findByDate(tradeDate);
    }

    public LimitCode findLastOne(String tradeDate) {
        return limitCodeRepo.findLastOne(tradeDate);
    }

    public Boolean checkDateHoliday(String nowDate) {
        return limitCodeRepo.checkDateHoliday(nowDate) > 0;
    }

}
