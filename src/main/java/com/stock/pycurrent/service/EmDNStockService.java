package com.stock.pycurrent.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.entity.vo.CodeDataVO;
import com.stock.pycurrent.entity.vo.LimitCodeVO;
import com.stock.pycurrent.repo.EmDNStockRepo;
import com.stock.pycurrent.schedule.PrepareData;
import com.stock.pycurrent.util.DateUtils;
import com.stock.pycurrent.util.JSONUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author fzc
 * @date 2024/6/13 15:55
 * @description
 */
@Service
public class EmDNStockService {
    @Resource
    private EmDNStockRepo emDNStockRepo;
    @Resource
    private EmRealTimeStockService emRealTimeStockService;

    public List<EmDNStock> findByCodeCount(String code) {
        return emDNStockRepo.findByCodeCount(code, DateUtils.getDateAtOffset(DateUtils.now(), -1, ChronoUnit.YEARS));
    }

    public List<CodeDataVO> findOPHC(Integer count, BigDecimal hand, BigDecimal pch) {
        Map<String, CodeDataVO> res = new HashMap<>();
        String tradeDate = emDNStockRepo.findMinTradeDateByCount(count);
        List<Object> tmp = emDNStockRepo.findOPHC(tradeDate, hand, pch);
        if (tmp != null && !tmp.isEmpty()) {
            tmp.forEach(x -> {
                Object[] tmpArr = (Object[]) x;
                CodeDataVO codeDataVO = new CodeDataVO();
                codeDataVO.setCode(tmpArr[0].toString());
                emRealTimeStockService.fillCodeData(codeDataVO);
                ObjectNode extraNode = JSONUtils.getNode();
                extraNode.put("hand", new BigDecimal(tmpArr[1].toString()));
                extraNode.put("hlc", new BigDecimal(tmpArr[2].toString()));
                codeDataVO.setExtraNode(extraNode);
                res.put(codeDataVO.getCode(), codeDataVO);
            });
            List<Object> tmp2 = emDNStockRepo.findStatisticByCodes(res.keySet().stream().toList(), tradeDate, count);
            if (tmp2 != null && !tmp2.isEmpty()) {
                tmp2.forEach(x -> {
                    Object[] tmpArr = (Object[]) x;
                    if (tmpArr != null) {
                        String code = tmpArr[0].toString();
                        CodeDataVO codeDataVO = res.get(code);
                        if (tmpArr[2] != null) {
                            codeDataVO.getExtraNode().put("ac", new BigDecimal(tmpArr[2].toString()));
                        }
                        if (tmpArr[1] != null) {
                            codeDataVO.getExtraNode().put("cc", new BigDecimal(tmpArr[1].toString()));
                        }
                    }
                });
            }
        }
        return new ArrayList<>(res.values());
    }
}
