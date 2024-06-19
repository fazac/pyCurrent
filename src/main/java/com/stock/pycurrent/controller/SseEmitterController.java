package com.stock.pycurrent.controller;


import com.stock.pycurrent.util.MySseEmitterUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author fzc
 * @date 2024/6/11 9:48
 * @description
 */
@RestController
@CrossOrigin
@RequestMapping("/sse")
public class SseEmitterController {


    @GetMapping("/createSSEConnect")
    public SseEmitter createSSEConnect(@RequestParam(value = "clientId", required = false) String clientId) {
        return MySseEmitterUtil.createSseConnect(clientId);
    }

}
