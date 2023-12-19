package com.stock.pycurrent.controller;


import com.stock.pycurrent.schedule.PullData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fzc
 * @date 2023/12/13 11:41
 * @description
 */
@RestController
@CrossOrigin
public class RealTimeController {

    private PullData pullData;

    @GetMapping("pullTest")
    public void pullTest() {
        pullData.pullTest();
    }

    @Autowired
    public void setPullData(PullData pullData) {
        this.pullData = pullData;
    }
}
