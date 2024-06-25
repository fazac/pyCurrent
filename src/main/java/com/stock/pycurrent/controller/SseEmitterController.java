package com.stock.pycurrent.controller;


import com.stock.pycurrent.entity.annotation.RequestLimit;
import com.stock.pycurrent.entity.emum.SSEMsgEnum;
import com.stock.pycurrent.entity.vo.RealTimeVO;
import com.stock.pycurrent.service.CurConcernCodeService;
import com.stock.pycurrent.service.EmRealTimeStockService;
import com.stock.pycurrent.service.RealBarService;
import com.stock.pycurrent.util.ArrayUtils;
import com.stock.pycurrent.util.ExecutorUtils;
import com.stock.pycurrent.util.MySseEmitterUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author fzc
 * @date 2024/6/11 9:48
 * @description
 */
@RestController
@RequestMapping("/sse")
public class SseEmitterController {
    @Resource
    private RealBarService realBarService;
    @Resource
    private EmRealTimeStockService emRealTimeStockService;
    @Resource
    private CurConcernCodeService curConcernCodeService;


    @GetMapping("/createSSEConnect")
    @RequestLimit(key = "limit0",
            permitsPerSecond = 3,
            timeout = 10,
            msg = "访问频率已超限制")
    public SseEmitter createSSEConnect(@RequestParam(value = "clientId", required = false) String clientId) {
        SseEmitter sseEmitter = MySseEmitterUtil.createSseConnect(clientId);
        if (!clientId.isEmpty()) {
            ExecutorUtils.SINGLE_MSG_POOL.schedule(this::sendRTMsg, 2, TimeUnit.SECONDS);
        } else {
            ExecutorUtils.SINGLE_MSG_POOL.schedule(this::sendConcernMsg, 2, TimeUnit.SECONDS);
        }
        return sseEmitter;
    }

    private void sendConcernMsg() {
        MySseEmitterUtil.sendMsgToClient(curConcernCodeService.findLast(), SSEMsgEnum.RT_CURRENT);
    }

    private void sendRTMsg() {
        if (!MySseEmitterUtil.codeCacheEmpty()) {
            MySseEmitterUtil.codeSSECache.keySet().forEach(x -> MySseEmitterUtil.sendMsgToClient(prepareRTHisData(x), SSEMsgEnum.RT_HIS));
        }
    }

    private List<RealTimeVO> prepareRTHisData(String code) {
        return ArrayUtils.convertRealTimeVO(
                emRealTimeStockService.findRBarStockByCode(code), realBarService.findIntradayBar(code));
    }

}