package com.stock.pycurrent.service;


import com.stock.pycurrent.entity.ContinuousUp;
import com.stock.pycurrent.entity.emum.PyFuncEnum;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.repo.ContinuousUpRepo;
import com.stock.pycurrent.schedule.PrepareData;
import com.stock.pycurrent.util.PythonScriptUtils;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

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

    @SuppressWarnings("unused")
    public void initContinuousUp() {
        PythonScriptUtils.execThreadPY(Constants.AKSHARE_STOCK_PY, PyFuncEnum.CONTINUOUS_UP.toString());
    }

    public List<ContinuousUp> findLastContinuous() {
        List<ContinuousUp> res = continuousUpRepo.findLast();
        if (res != null && !res.isEmpty()) {
            res.forEach(x -> x.setLabels(PrepareData.findLabelStr(x.getTsCode())));
        }
        return res;
    }
}
