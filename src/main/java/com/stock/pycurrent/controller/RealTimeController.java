package com.stock.pycurrent.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.pycurrent.entity.model.Constants;
import com.stock.pycurrent.entity.vo.DnVO;
import com.stock.pycurrent.service.EmDNStockService;
import com.stock.pycurrent.service.EmRealTimeStockService;
import com.stock.pycurrent.service.RealBarService;
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
import java.util.List;

/**
 * @author fzc
 * @date 2024/6/11 17:00
 * @description
 */

@RestController
@RequestMapping("rt")
@CommonsLog
@CrossOrigin
public class RealTimeController {
    @Resource
    private EmRealTimeStockService emRealTimeStockService;
    @Resource
    private EmDNStockService emDNStockService;
    @Resource
    private RealBarService realBarService;

    @GetMapping("findDataByCode")
    public ObjectNode findDataByCode(@Param("code") String code) {
        ObjectNode objectNode = JSONUtils.getNode();
        List<DnVO> dnVOList = ArrayUtils.convertDnVO(emDNStockService.findByCodeCount(code, 30).reversed());
        objectNode.putPOJO("dnData", dnVOList);
        objectNode.putPOJO("dnPriMin", findPriMin(dnVOList));
        objectNode.putPOJO("dnPriMax", findPriMax(dnVOList));
        return objectNode;
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
