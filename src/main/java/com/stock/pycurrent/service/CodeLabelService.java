package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.CodeLabel;
import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.repo.BoardConceptConRepo;
import com.stock.pycurrent.repo.BoardIndustryConRepo;
import com.stock.pycurrent.repo.CodeLabelRepo;
import com.stock.pycurrent.util.DateUtils;
import com.stock.pycurrent.util.StockUtils;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fzc
 * @date 2024/6/27 14:41
 * @description
 */
@Service
@CommonsLog
public class CodeLabelService {
    @Resource
    private CodeLabelRepo codeLabelRepo;
    @Resource
    private BoardConceptConRepo boardConceptConRepo;
    @Resource
    private BoardIndustryConRepo boardIndustryConRepo;
    @Resource
    private EmRealTimeStockService emRealTimeStockService;

    public List<CodeLabel> findLast() {
        return codeLabelRepo.findLast();
    }

    public CodeLabel findByCode(String code) {
        return codeLabelRepo.findByCode(code);
    }

    public void saveList(List<CodeLabel> codeLabels) {
        if (codeLabels != null && !codeLabels.isEmpty()) {
            codeLabelRepo.saveAllAndFlush(codeLabels);
        }
    }

    public void createLabels() {
        String now = DateUtils.now();
        String lastDate = codeLabelRepo.findMaxDate();
//        if (now.equals(lastDate) || !StockUtils.afterPullHour()) {
//            return;
//        }
        List<EmRealTimeStock> emRealTimeStockList = emRealTimeStockService.findLast();
        Map<String, String> codeNameMap;
        if (emRealTimeStockList != null && !emRealTimeStockList.isEmpty()) {
            codeNameMap = emRealTimeStockList.stream().collect(Collectors.toMap(EmRealTimeStock::getTsCode, EmRealTimeStock::getName));
        } else {
            codeNameMap = new HashMap<>();
        }
        Map<String, CodeLabel> codeLabelMap = new HashMap<>();
        List<Object> conceptList = boardConceptConRepo.findLast();
        if (conceptList != null && !conceptList.isEmpty()) {
            conceptList.forEach(x -> {
                Object[] tmpArr = (Object[]) x;
                CodeLabel codeLabel = new CodeLabel();
                codeLabel.setTradeDate(now);
                codeLabel.setTsCode(tmpArr[0].toString());
                codeLabel.setConcept(tmpArr[1] == null ? "" : tmpArr[1].toString());
                codeLabel.setName(codeNameMap.getOrDefault(codeLabel.getTsCode(), ""));
                codeLabelMap.put(codeLabel.getTsCode(), codeLabel);
            });
        }
        List<Object> industryList = boardIndustryConRepo.findLast();
        if (industryList != null && !industryList.isEmpty()) {
            industryList.forEach(x -> {
                Object[] tmpArr = (Object[]) x;
                if (codeLabelMap.containsKey(tmpArr[0].toString())) {
                    CodeLabel codeLabel = codeLabelMap.get(tmpArr[0].toString());
                    codeLabel.setIndustry(tmpArr[1] == null ? "" : tmpArr[1].toString());
                } else {
                    CodeLabel codeLabel = new CodeLabel();
                    codeLabel.setTradeDate(now);
                    codeLabel.setTsCode(tmpArr[0].toString());
                    codeLabel.setConcept("");
                    codeLabel.setIndustry(tmpArr[1] == null ? "" : tmpArr[1].toString());
                    codeLabel.setName(codeNameMap.getOrDefault(codeLabel.getTsCode(), ""));
                    codeLabelMap.put(codeLabel.getTsCode(), codeLabel);
                }
            });
        }
        if (!codeLabelMap.isEmpty()) {
            saveList(new ArrayList<>(codeLabelMap.values()));
        }
    }
}
