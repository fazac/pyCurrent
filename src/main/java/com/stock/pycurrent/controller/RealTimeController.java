package com.stock.pycurrent.controller;

import com.stock.pycurrent.entity.EmRealTimeStock;
import com.stock.pycurrent.service.EmRealTimeStockService;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fzc
 * @date 2024/6/11 17:00
 * @description
 */

@RestController
@CommonsLog
public class RealTimeController {
    @Resource
    private EmRealTimeStockService emRealTimeStockService;

    @GetMapping("findByCode")
    public List<EmRealTimeStock> findByCode(@Param("code") String code) {

    }
}
