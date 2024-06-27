package com.stock.pycurrent.service;


import com.stock.pycurrent.entity.emum.EMSymbolEnum;
import com.stock.pycurrent.entity.emum.PyFuncEnum;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.repo.BoardConceptConRepo;
import com.stock.pycurrent.util.PythonScriptUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author fzc
 * @date 2023/5/15 15:41
 * @description
 */
@Service
public class BoardConceptConService {
    @Resource
    private BoardConceptConRepo boardConceptConRepo;

    public void findBoardConceptConCurrent() {
        PythonScriptUtils.execThreadPY(Constants.AKSHARE_EM_HIS, PyFuncEnum.EM_BOARD_CONCEPT_CON + EMSymbolEnum.EM_CONCEPT.toString(), null);
    }

    @SuppressWarnings("unused")
    public String findConceptByCode(String code) {
        return boardConceptConRepo.findByCode(code);
    }

}
