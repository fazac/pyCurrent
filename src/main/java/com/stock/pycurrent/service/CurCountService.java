package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.CurCount;
import com.stock.pycurrent.repo.CurCountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fzc
 * @date 2024/1/29 10:52
 * @description
 */
@Service
public class CurCountService {
    private CurCountRepo curCountRepo;

    public void saveOne(CurCount curCount) {
        curCountRepo.saveAndFlush(curCount);
    }

    @Autowired
    public void setCurCountRepo(CurCountRepo curCountRepo) {
        this.curCountRepo = curCountRepo;
    }
}
