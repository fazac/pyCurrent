package com.stock.pycurrent.util;

import com.stock.pycurrent.entity.emum.SSEMsgEnum;
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
    public static final Map<String, SseEmitter> clientSSECache = new ConcurrentHashMap<>();
    public static final Map<String, SseEmitter> codeSSECache = new ConcurrentHashMap<>();

    public static boolean codeCacheEmpty() {
        return codeSSECache.isEmpty();
    }

    public static SseEmitter createSseConnect(String clientId) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        if (clientId.isBlank()) {
            closeMapConnect(clientSSECache);
            clientId = UUID.randomUUID().toString();
            clientSSECache.put(clientId, sseEmitter);
        } else {
            closeMapConnect(codeSSECache);
            codeSSECache.put(clientId, sseEmitter);
        }
        sseEmitter.onCompletion(completionCallBack(clientId));
        try {
            sseEmitter.send(SseEmitter.event().id(Constants.CLIENT_ID).data(clientId));
        } catch (IOException e) {
            log.error("MySseEmitterService[createSseConnect]: 创建长链接异常，客户端ID:" + clientId, e);
        }
        log.warn("ClientSSECacheSize: " + clientSSECache.size());
        log.warn("CodeSSECacheSize: " + clientSSECache.size());
        return sseEmitter;
    }

    public static void closeMapConnect(Map<String, SseEmitter> sseCache) {
        if (!sseCache.isEmpty()) {
            for (Map.Entry<String, SseEmitter> entry : sseCache.entrySet()) {
                entry.getValue().complete();
            }
            sseCache.clear();
        }
    }

    @SuppressWarnings("rawtypes")
    public static void sendMsgToClient(List data, SSEMsgEnum sseMsgEnum) {
        if (data == null || data.isEmpty()) {
            return;
        }
        switch (sseMsgEnum) {
            case SSEMsgEnum.RT_CURRENT:
                if (clientSSECache.isEmpty()) {
                    return;
                }
                for (Map.Entry<String, SseEmitter> entry : clientSSECache.entrySet()) {
                    sendMsgToClientByClientId(entry.getKey(), Constants.SSE_RT_LIST, data, entry.getValue());
                }
                break;
            case SSEMsgEnum.RT_HIS:
                if (codeSSECache.isEmpty()) {
                    return;
                }
                for (Map.Entry<String, SseEmitter> entry : codeSSECache.entrySet()) {
                    sendMsgToClientByClientId(entry.getKey(), Constants.SSE_RT_HIS, data, entry.getValue());
                }
                break;
        }
    }

    /**
     * 推送消息到客户端
     *
     * @param clientId 客户端ID
     **/
    @SuppressWarnings("rawtypes")
    private static void sendMsgToClientByClientId(String clientId, String id, List data, SseEmitter sseEmitter) {
        if (sseEmitter == null) {
            return;
        }
        SseEmitter.SseEventBuilder sendData = SseEmitter.event().id(id).data(data, MediaType.APPLICATION_JSON);
        try {
            sseEmitter.send(sendData);
        } catch (IOException e) {
            closeSseConnect(clientId);
        }
    }

    public static void closeSseConnect(String clientId) {
        SseEmitter sseEmitter = clientId.length() > 6 ? clientSSECache.get(clientId) : codeSSECache.get(clientId);
        if (sseEmitter != null) {
            sseEmitter.complete();
            removeClient(clientId);
        }
    }

    /**
     * 长链接完成后回调接口(即关闭连接时调用)
     *
     * @param clientId 客户端ID
     * @return java.lang.Runnable
     **/
    private static Runnable completionCallBack(String clientId) {
        return () -> removeClient(clientId);
    }


    /**
     * 移除用户连接
     *
     * @param clientId 客户端ID
     **/
    private static void removeClient(String clientId) {
        if (clientId.length() > 6) {
            clientSSECache.remove(clientId);
        } else {
            codeSSECache.remove(clientId);
        }
    }
}