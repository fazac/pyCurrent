package com.stock.pycurrent.controller;

import com.stock.pycurrent.entity.EmConstant;
import com.stock.pycurrent.entity.jsonvalue.EmConstantValue;
import com.stock.pycurrent.service.EmConstantService;
import com.stock.pycurrent.util.JSONUtils;
import jakarta.annotation.Resource;
import jdk.jshell.EvalException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fzc
 * @date 2024/6/25 10:49
 * @description
 */
@RestController
@RequestMapping("constants")
@CommonsLog
public class ConstantsController {
    @Resource
    private EmConstantService emConstantService;

    @GetMapping("findAll")
    public List<EmConstant> findAll() {
        return emConstantService.findAll();
    }

    @PostMapping("updateOne")
    public String updateOne(@RequestBody EmConstant emConstant) throws IOException {
        if (emConstant.getMultiValueStr() != null && !emConstant.getMultiValueStr().isEmpty()) {
            emConstant.setMultiValue(JSONUtils.fromJsonList(EmConstantValue.class, emConstant.getMultiValueStr()));
        }
        emConstantService.updateOne(emConstant);
        return "ok";
    }
}
