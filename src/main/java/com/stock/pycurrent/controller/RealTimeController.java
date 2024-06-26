package com.stock.pycurrent.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.pycurrent.entity.CurConcernCode;
import com.stock.pycurrent.entity.annotation.RequestLimit;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.entity.vo.DnVO;
import com.stock.pycurrent.service.*;
import com.stock.pycurrent.util.ArrayUtils;
import com.stock.pycurrent.util.JSONUtils;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fzc
 * @date 2024/6/11 17:00
 * @description
 */

@RestController
@RequestMapping("rt")
@CommonsLog
public class RealTimeController {
    @Resource
    private EmRealTimeStockService emRealTimeStockService;
    @Resource
    private EmDNStockService emDNStockService;
    @Resource
    private BoardIndustryConService boardIndustryConService;
    @Resource
    private BoardConceptConService boardConceptConService;
    @Resource
    private RocModelService rocModelService;
    @Resource
    private CurConcernCodeService curConcernCodeService;

    public RealTimeController() {
    }

    @GetMapping("findDataByCode")
    @RequestLimit(key = "limit0",
            permitsPerSecond = 1,
            timeout = 500,
            msg = "访问频率已超限制")
    public ObjectNode findDataByCode(@Param("code") String code) {
        ObjectNode objectNode = JSONUtils.getNode();
        List<String> labels = new ArrayList<>(Arrays.asList(boardConceptConService.findConceptByCode(code).split(",")));
        labels.addFirst(boardIndustryConService.findIndustryByCode(code));
        objectNode.putPOJO("label", labels);
        objectNode.putPOJO("open", ArrayUtils.convertOpenVO(emRealTimeStockService.findOpenByCode(code)));
        objectNode.putPOJO("dnDetail", emDNStockService.findByCodeCount(code, 30));
        objectNode.putPOJO("roc", rocModelService.findRocByCode(code));
        return objectNode;
    }

    @GetMapping("findDataLineByCode")
    @RequestLimit(key = "limit1",
            permitsPerSecond = 1,
            timeout = 500,
            msg = "访问频率已超限制")
    public ObjectNode findDataLineByCode(@Param("code") String code) {
        ObjectNode objectNode = JSONUtils.getNode();
        List<DnVO> dnVOList = ArrayUtils.convertDnVO(emDNStockService.findByCodeCount(code, 30).reversed());
        objectNode.putPOJO("dnData", dnVOList);
        objectNode.putPOJO("dnPriMin", findPriMin(dnVOList));
        objectNode.putPOJO("dnPriMax", findPriMax(dnVOList));
        return objectNode;
    }

    @GetMapping("findOtherList")
    public List<CurConcernCode> findOtherList() {
        return curConcernCodeService.findLast("0");
    }

    public BigDecimal findPriMin(List<DnVO> dnVOList) {
        BigDecimal min = Constants.PRICE_MAX;
        for (DnVO dnVO : dnVOList) {
            min = min.min(dnVO.getLp());
        }
        return min.setScale(0, RoundingMode.FLOOR);
    }

    public BigDecimal findPriMax(List<DnVO> dnVOList) {
        BigDecimal max = Constants.PRICE_MIN;
        for (DnVO dnVO : dnVOList) {
            max = max.max(dnVO.getHp());
        }
        return max.setScale(0, RoundingMode.CEILING);
    }

}
