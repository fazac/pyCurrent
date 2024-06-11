package com.stock.pycurrent.util;

import com.stock.pycurrent.entity.CurConcernCode;
import com.stock.pycurrent.entity.model.Constants;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fzc
 * @date 2024/6/11 9:48
 * @description
 */

@Component
@CommonsLog
public class MySseEmitterUtil {
    /**
     * 容器，保存连接，用于输出返回
     */
    private static final Map<String, SseEmitter> sseCache = new ConcurrentHashMap<>();

    public static SseEmitter createSseConnect(String clientId) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        // 是否需要给客户端推送ID
        if (clientId.isBlank()) {
            clientId = UUID.randomUUID().toString();
        }
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(clientId));
        sseCache.put(clientId, sseEmitter);

        try {
            sseEmitter.send(SseEmitter.event().id(Constants.CLIENT_ID).data(clientId));
        } catch (IOException e) {
            log.error("MySseEmitterService[createSseConnect]: 创建长链接异常，客户端ID:" + clientId, e);
        }
        return sseEmitter;
    }

    public static void closeSseConnect(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            if (!sseCache.isEmpty()) {
                for (Map.Entry<String, SseEmitter> entry : sseCache.entrySet()) {
                    entry.getValue().complete();
                    removeClient(entry.getKey());
                }
            }
        } else {
            SseEmitter sseEmitter = sseCache.get(clientId);
            if (sseEmitter != null) {
                sseEmitter.complete();
                removeClient(clientId);
            }
        }
    }

    public static void sendMsgToClient(List<CurConcernCode> curConcernCodeList) {
        if (sseCache.isEmpty()) {
            return;
        }
        for (Map.Entry<String, SseEmitter> entry : sseCache.entrySet()) {
            sendMsgToClientByClientId(entry.getKey(), curConcernCodeList, entry.getValue());
        }
    }

    /**
     * 推送消息到客户端
     * 此处做了推送失败后，重试推送机制，可根据自己业务进行修改
     *
     * @param clientId           客户端ID
     * @param curConcernCodeList 推送信息，此处结合具体业务，定义自己的返回值即可
     **/
    private static void sendMsgToClientByClientId(String clientId, List<CurConcernCode> curConcernCodeList, SseEmitter sseEmitter) {
        if (sseEmitter == null) {
            return;
        }
        SseEmitter.SseEventBuilder sendData = SseEmitter.event().id(Constants.TASK_RESULT).data(curConcernCodeList, MediaType.APPLICATION_JSON);
        try {
            sseEmitter.send(sendData);
        } catch (Exception e) {
            closeSseConnect(clientId);
        }
    }

    /**
     * 长链接完成后回调接口(即关闭连接时调用)
     *
     * @param clientId 客户端ID
     * @return java.lang.Runnable
     **/
    private static Runnable completionCallBack(String clientId) {
        return () -> {
            removeClient(clientId);
        };
    }


    /**
     * 移除用户连接
     *
     * @param clientId 客户端ID
     **/
    private static void removeClient(String clientId) {
        sseCache.remove(clientId);
    }
}