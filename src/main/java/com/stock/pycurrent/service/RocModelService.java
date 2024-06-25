package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.RocModel;
import com.stock.pycurrent.repo.RocModelRepo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fzc
 * @date 2024/6/25 10:43
 * @description
 */
@Service
public class RocModelService {
    @Resource
    private RocModelRepo rocModelRepo;

    public List<RocModel> findRocByCode(String code) {
        return rocModelRepo.findByCode(code);
    }
}
