package com.stock.pycurrent.service;

import com.stock.pycurrent.repo.YfUsrRepo;
import jakarta.annotation.Resource;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class YfUsrService {
    @Resource
    private YfUsrRepo yfUsrRepo;


}



