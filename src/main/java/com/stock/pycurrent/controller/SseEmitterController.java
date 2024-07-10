package com.stock.pycurrent.controller;


import com.stock.pycurrent.entity.annotation.RequestLimit;
import com.stock.pycurrent.entity.emum.SSEMsgEnum;
import com.stock.pycurrent.entity.vo.RealTimeVO;
import com.stock.pycurrent.service.CurConcernCodeService;
import com.stock.pycurrent.service.EmRealTimeStockService;
import com.stock.pycurrent.service.RealBarService;
import com.stock.pycurrent.util.ArrayUtils;
import com.stock.pycurrent.util.ExecutorUtils;
import com.stock.pycurrent.util.MessageUtil;
import com.stock.pycurrent.util.MySseEmitterUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Objects;
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

    @GetMapping("testMsg")
    public void testMsg() {
        MessageUtil.sendNotificationMsg("hello", "new One");
    }

    @GetMapping("/createSSEConnect")
    @RequestLimit(key = "limit0",
            permitsPerSecond = 3,
            timeout = 10,
            msg = "访问频率已超限制")
    public SseEmitter createSSEConnect(@RequestParam(value = "clientId", required = false) String clientId,
                                       @RequestParam(value = "type") String type) {
        SSEMsgEnum sseMsgEnum = Objects.requireNonNull(SSEMsgEnum.fromType(type));
        SseEmitter sseEmitter = MySseEmitterUtil.createSseConnect(clientId, sseMsgEnum);
        switch (sseMsgEnum) {
            case RT_CURRENT -> {
                ExecutorUtils.SINGLE_MSG_POOL.schedule(this::sendConcernMsg, 500, TimeUnit.MILLISECONDS);
            }
            case RT_HIS -> {
                ExecutorUtils.SINGLE_MSG_POOL.schedule(this::sendRTMsg, 500, TimeUnit.MILLISECONDS);
            }
        }
        return sseEmitter;
    }

    private void sendConcernMsg() {
        MySseEmitterUtil.sendMsgToClient(curConcernCodeService.findLast("1"), SSEMsgEnum.RT_CURRENT);
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
