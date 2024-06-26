package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.CurCount;
import com.stock.pycurrent.repo.CurCountRepo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return curCountRepo.findLastAll();
    }

}
