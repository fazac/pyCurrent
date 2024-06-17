package com.stock.pycurrent.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
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

    @GetMapping("findByCode")
    public ObjectNode findByCode(@Param("code") String code) {
        ObjectNode objectNode = JSONUtils.getNode();
        objectNode.putPOJO("dnData", emDNStockService.findByCodeCount(code, 30).reversed());
        objectNode.putPOJO("rtData", ArrayUtils.convertRealTimeVO(
                emRealTimeStockService.findRBarStockByCode(code), realBarService.findIntradayBar(code)));
        return objectNode;
    }
}
