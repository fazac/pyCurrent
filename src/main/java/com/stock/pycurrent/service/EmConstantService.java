package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.EmConstant;
import com.stock.pycurrent.repo.EmConstantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author fzc
 * @date 2023/11/12 9:59
 * @description
 */
@Service
public class EmConstantService {
    private EmConstantRepo emConstantRepo;

    public List<EmConstant> findAll() {
        return emConstantRepo.findAll();
    }

    public EmConstant findOneByKey() {
        return emConstantRepo.findByKey("NOTIFICATION");
    }

    @Autowired
    public void setEmConstantRepo(EmConstantRepo emConstantRepo) {
        this.emConstantRepo = emConstantRepo;
    }
}
