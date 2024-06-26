package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.entity.vo.LimitCodeVO;
import com.stock.pycurrent.repo.EmDNStockRepo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        List<LimitCodeVO> res = new ArrayList<>();
        List<Object> tmp = emDNStockRepo.findOPHC(count, hand, pch);
        if (tmp != null && !tmp.isEmpty()) {
            tmp.forEach(x -> {
                Object[] tmpArr = (Object[]) x;
                LimitCodeVO limitCodeVO = new LimitCodeVO();
                limitCodeVO.setCode(tmpArr[0].toString());
                limitCodeVO.setHand(new BigDecimal(tmpArr[1].toString()));
                limitCodeVO.setHlc(new BigDecimal(tmpArr[2].toString()));
                res.add(limitCodeVO);
            });
        }
        return res;
    }
}
