package com.stock.pycurrent.service;

import com.stock.pycurrent.entity.BoardCode;
import com.stock.pycurrent.repo.BoardCodeRepo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author fzc
 * @date 2024/6/5 11:37
 * @description
 */
@Service
public class BoardCodeService {
    @Resource
    private BoardCodeRepo boardCodeRepo;

    public BoardCode findByDate(String tradeDate) {
        return boardCodeRepo.findByDate(tradeDate);
    }

    public void saveEntity(BoardCode boardCode) {
        boardCodeRepo.saveAndFlush(boardCode);
    }
}
