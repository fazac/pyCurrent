package com.stock.pycurrent.controller;

import com.stock.pycurrent.entity.LastHandPri;
import com.stock.pycurrent.service.LastHandPriService;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fzc
 * @date 2024/7/8 14:04
 * @description
 */
@RestController
@RequestMapping("lhp")
@CommonsLog
public class LastHandPriController {
    @Resource
    private LastHandPriService lastHandPriService;

    @GetMapping("findLHPByCode")
    public List<LastHandPri> findLHPByCode(@RequestParam(value = "code") String code) {
        return lastHandPriService.findLHPByCode(code);
    }

    @PostMapping("createPastRecord")
    public void createPastRecord() {
        lastHandPriService.createRecode();
    }
}
