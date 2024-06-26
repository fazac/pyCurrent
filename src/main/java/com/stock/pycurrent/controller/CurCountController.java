package com.stock.pycurrent.controller;

import com.stock.pycurrent.entity.CurCount;
import com.stock.pycurrent.service.CurCountService;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fzc
 * @date 2024/6/26 9:58
 * @description
 */
@RestController
@RequestMapping("curcount")
@CommonsLog
public class CurCountController {
    @Resource
    private CurCountService curCountService;

    @GetMapping("findLastAll")
    public List<CurCount> findLastAll() {
        return curCountService.findLastAll();
    }

}
