package com.stock.pycurrent.service;


import com.stock.pycurrent.entity.emum.EMSymbolEnum;
import com.stock.pycurrent.entity.emum.PyFuncEnum;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.repo.BoardIndustryConRepo;
import com.stock.pycurrent.util.PythonScriptUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author fzc
 * @date 2023/5/15 15:41
 * @description
 */
@Service
public class BoardIndustryConService {
    @Resource
    private BoardIndustryConRepo boardIndustryConRepo;

    public void findBoardIndustryConCurrent() {
        PythonScriptUtils.execThreadPY(Constants.AKSHARE_EM_HIS, PyFuncEnum.EM_BOARD_INDUSTRY_CON + EMSymbolEnum.EM_INDUSTRY.toString(), null);
    }

    @SuppressWarnings("unused")
    public String findIndustryByCode(String code) {
        return boardIndustryConRepo.findByCode(code);
    }
}
