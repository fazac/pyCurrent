package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.RocModel;
import com.stock.pycurrent.entity.vo.LimitCodeVO;
import com.stock.pycurrent.exception.MyException;
import com.stock.pycurrent.repo.RocModelRepo;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fzc
 * @date 2024/6/25 10:43
 * @description
 */
@Service
@CommonsLog
public class RocModelService {
    @Resource
    private RocModelRepo rocModelRepo;

    public List<RocModel> findRocByCode(String code) {
        return rocModelRepo.findByCode(code);
    }

    public List<LimitCodeVO> findRocByLimit(BigDecimal r2LowLimit,
                                            BigDecimal r2HighLimit,
                                            BigDecimal r1LowLimit,
                                            BigDecimal r1HighLimit) {
        List<LimitCodeVO> res = new ArrayList<>();
        List<Object> tmpRes = rocModelRepo.findByLimit(r2LowLimit, r2HighLimit, r1LowLimit, r1HighLimit);
        if (tmpRes != null && !tmpRes.isEmpty()) {
            if (tmpRes.size() >= 250) {
                throw new MyException("结果超过250条");
            }
            tmpRes.forEach(x -> {
                Object[] tmpArr = (Object[]) x;
                LimitCodeVO limitCodeVO = new LimitCodeVO();
                limitCodeVO.setCode(tmpArr[0].toString());
                if (tmpArr[1] != null) {
                    limitCodeVO.setR1(new BigDecimal(tmpArr[1].toString()));
                }
                if (tmpArr[2] != null) {
                    limitCodeVO.setR2(new BigDecimal(tmpArr[2].toString()));
                }
                res.add(limitCodeVO);
            });

        }
        return res;
    }
}
