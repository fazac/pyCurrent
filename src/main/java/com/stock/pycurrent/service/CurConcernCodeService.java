package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.CurConcernCode;
import com.stock.pycurrent.repo.CurConcernCodeRepo;
import com.stock.pycurrent.schedule.PrepareData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fzc
 * @date 2024/6/11 14:05
 * @description
 */
@Service
public class CurConcernCodeService {
    @Resource
    private CurConcernCodeRepo curConcernCodeRepo;

    public void saveList(List<CurConcernCode> concernCodeList) {
        curConcernCodeRepo.saveAllAndFlush(concernCodeList);
    }

    public List<CurConcernCode> findLast(String type) {
        List<CurConcernCode> curConcernCodeList = curConcernCodeRepo.findLast(type);
        if (curConcernCodeList != null && !curConcernCodeList.isEmpty()) {
            curConcernCodeList.forEach(x -> {
                x.setLabels(PrepareData.findLabelStr(x.getTsCode()));
            });
        }
        return curConcernCodeList;
    }
}
