package com.stock.pycurrent.service;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.pycurrent.entity.ContinuousUp;
import com.stock.pycurrent.entity.emum.PyFuncEnum;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.entity.vo.CodeDataVO;
import com.stock.pycurrent.repo.ContinuousUpRepo;
import com.stock.pycurrent.util.JSONUtils;
import com.stock.pycurrent.util.PythonScriptUtils;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fzc
 * @date 2023/10/30 14:37
 * @description
 */
@Service
@CommonsLog
public class ContinuousUpService {
    @Resource
    private ContinuousUpRepo continuousUpRepo;
    @Resource
    private EmRealTimeStockService emRealTimeStockService;

    @SuppressWarnings("unused")
    public void initContinuousUp() {
        PythonScriptUtils.execThreadPY(Constants.AKSHARE_STOCK_PY, PyFuncEnum.CONTINUOUS_UP.toString());
    }

    public List<CodeDataVO> findLastContinuous() {
        List<CodeDataVO> codeDataVOList = new ArrayList<>();
        List<ContinuousUp> res = continuousUpRepo.findLast();
        if (res != null && !res.isEmpty()) {
            res.forEach(x -> {
                CodeDataVO codeDataVO = new CodeDataVO();
                codeDataVO.setCode(x.getTsCode());
                emRealTimeStockService.fillCodeData(codeDataVO);
                ObjectNode extraNode = JSONUtils.getNode();
                extraNode.put("upd", x.getUpDays());
                extraNode.put("upp", x.getUpPer());
                extraNode.put("uhand", x.getChangeHand());
                codeDataVO.setExtraNode(extraNode);
                codeDataVOList.add(codeDataVO);
            });
        }
        return codeDataVOList;
    }
}
