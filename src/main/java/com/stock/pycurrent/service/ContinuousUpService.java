package com.stock.pycurrent.service;


import com.stock.pycurrent.entity.emum.PyFuncEnum;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.util.PythonScriptUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

/**
 * @author fzc
 * @date 2023/10/30 14:37
 * @description
 */
@Service
@CommonsLog
public class ContinuousUpService {
    public void initContinuousUp() {
        PythonScriptUtils.execThreadPY(Constants.AKSHARE_STOCK_PY, PyFuncEnum.CONTINUOUS_UP.toString());
    }
}
