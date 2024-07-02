package com.stock.pycurrent.controller;

import com.stock.pycurrent.entity.ContinuousUp;
import com.stock.pycurrent.service.ContinuousUpService;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("continuous")
@CommonsLog
public class ContinuousController {
    @Resource
    private ContinuousUpService continuousUpService;

    @GetMapping("findLastContinuous")
    public List<ContinuousUp> findLastContinuous() {
        return continuousUpService.findLastContinuous();
    }
}
