package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.RangeOverCode;
import com.stock.pycurrent.repo.RangeOverCodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fzc
 * @date 2023/12/22 10:08
 * @description
 */
@Service
public class RangeOverCodeService {
    private RangeOverCodeRepo rangeOverCodeRepo;

    public RangeOverCode findByDate(String tradeDate) {
        return rangeOverCodeRepo.findByDate(tradeDate);
    }

    public void saveEntity(RangeOverCode rangeOverCode) {
        rangeOverCodeRepo.saveAndFlush(rangeOverCode);
    }

    @Autowired
    public void setRangeOverCodeRepo(RangeOverCodeRepo rangeOverCodeRepo) {
        this.rangeOverCodeRepo = rangeOverCodeRepo;
    }
}
