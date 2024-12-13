package com.stock.pycurrent.controller;

import com.stock.pycurrent.entity.LastHandPri;
import com.stock.pycurrent.service.LastHandPriService;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.context.properties.bind.DefaultValue;
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
    public List<LastHandPri> findLHPByCode(@RequestParam(value = "code") String code, @RequestParam(value = "type") @DefaultValue("1") int type) {
        return lastHandPriService.findLHPByCode(code, type);
    }

}
