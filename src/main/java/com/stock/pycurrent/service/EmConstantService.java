package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmConstant;
import com.stock.pycurrent.repo.EmConstantRepo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fzc
 * @date 2023/11/12 9:59
 * @description
 */
@Service
public class EmConstantService {
    @Resource
    private EmConstantRepo emConstantRepo;

    public List<EmConstant> findAll() {
        return emConstantRepo.findAll();
    }

    public void updateOne(EmConstant emConstant) {
        emConstantRepo.saveAndFlush(emConstant);
    }

    public EmConstant findOneByKey() {
        return emConstantRepo.findByKey("NOTIFICATION");
    }

}
