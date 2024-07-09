package com.stock.pycurrent.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.pycurrent.entity.CurConcernCode;
import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.entity.vo.CodeDataVO;
import com.stock.pycurrent.repo.CurConcernCodeRepo;
import com.stock.pycurrent.schedule.PrepareData;
import com.stock.pycurrent.util.JSONUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Resource
    private EmRealTimeStockService emRealTimeStockService;

    public void saveList(List<CurConcernCode> concernCodeList) {
        curConcernCodeRepo.saveAllAndFlush(concernCodeList);
    }

    public List<CodeDataVO> findLast(String type) {
        List<CodeDataVO> codeDataVOList = new ArrayList<>();
        List<CurConcernCode> curConcernCodeList = curConcernCodeRepo.findLast(type);
        if (curConcernCodeList != null && !curConcernCodeList.isEmpty()) {
            boolean brFlag = curConcernCodeList.stream().anyMatch(x -> x.getBp() != null);
            curConcernCodeList.forEach(x -> {
                CodeDataVO codeDataVO = new CodeDataVO();
                codeDataVO.setCode(x.getTsCode());
                emRealTimeStockService.fillCodeData(codeDataVO);
                ObjectNode extraNode = JSONUtils.getNode();
                extraNode.put("mark", x.getMark());
                extraNode.put("rt", x.getRt());
                extraNode.put("h", x.getH());
                if (brFlag) {
                    extraNode.put("rr", x.getRr());
                    extraNode.put("bp", x.getBp());
                }
                extraNode.put("bar", x.getBar());
                codeDataVO.setExtraNode(extraNode);
                codeDataVOList.add(codeDataVO);
            });
        }
        return codeDataVOList;
    }
}
