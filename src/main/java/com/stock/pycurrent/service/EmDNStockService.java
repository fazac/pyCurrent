package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmDNStock;
import com.stock.pycurrent.repo.EmDNStockRepo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fzc
 * @date 2024/6/13 15:55
 * @description
 */
@Service
public class EmDNStockService {
    @Resource
    private EmDNStockRepo emDNStockRepo;

    public List<EmDNStock> findByCodeCount(String code, int count) {
        return emDNStockRepo.findByCodeCount(code, count);
    }

}
