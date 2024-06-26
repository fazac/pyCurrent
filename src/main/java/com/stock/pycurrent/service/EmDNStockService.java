package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.vo.LimitCodeVO;
import com.stock.pycurrent.repo.EmDNStockRepo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public List<EmDNStock> findByCodeCount(String code, int count) {
        return emDNStockRepo.findByCodeCount(code, count);
    }

    public List<LimitCodeVO> findOPHC(Integer count, BigDecimal hand, BigDecimal pch) {
        Map<String, LimitCodeVO> res = new HashMap<>();
        String tradeDate = emDNStockRepo.findMinTradeDateByCount(count);
        List<Object> tmp = emDNStockRepo.findOPHC(tradeDate, hand, pch);
        if (tmp != null && !tmp.isEmpty()) {
            tmp.forEach(x -> {
                Object[] tmpArr = (Object[]) x;
                LimitCodeVO limitCodeVO = new LimitCodeVO();
                limitCodeVO.setCode(tmpArr[0].toString());
                limitCodeVO.setHand(new BigDecimal(tmpArr[1].toString()));
                limitCodeVO.setHlc(new BigDecimal(tmpArr[2].toString()));
                res.put(limitCodeVO.getCode(), limitCodeVO);
            });
            List<Object> tmp2 = emDNStockRepo.findStatisticByCodes(res.keySet().stream().toList(), tradeDate, count);
            if (tmp2 != null && !tmp2.isEmpty()) {
                tmp2.forEach(x -> {
                    Object[] tmpArr = (Object[]) x;
                    if (tmpArr != null) {
                        String code = tmpArr[0].toString();
                        LimitCodeVO limitCodeVO = res.get(code);
                        if (tmpArr[2] != null) {
                            limitCodeVO.setAc(new BigDecimal(tmpArr[2].toString()));
                        }
                        if (tmpArr[1] != null) {
                            limitCodeVO.setCc(new BigDecimal(tmpArr[1].toString()));
                        }
                    }
                });
            }
        }
        return res.values().stream().sorted(Comparator.comparing(LimitCodeVO::getHlc)).toList().reversed();
    }
}
