package com.stock.pycurrent.util;

import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;

/**
 * @author fzc
 * @date 2023/11/16 12:01
 * @description
 */
@CommonsLog
public class MessageUtil {
    private MessageUtil() {
        throw new IllegalStateException("MessageUtil class");
    }

    @SneakyThrows
    public static void sendNotificationMsg(String title, String code) {
        log.warn(title + " " + code);
    }


}
