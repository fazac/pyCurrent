package com.stock.pycurrent.util;

/**
 * @author fzc
 * @date 2023/5/19 14:01
 * @description
 */

public class PARAMS {
    /**
     * 是否为正式服务器, 由jvm参数来控制 (服务启动项(VM options)添加 -Ddata.env=bak)
     */
    public static final boolean BAK_MODE;

    static {
        BAK_MODE = "bak".equals(System.getProperty("data.env"));
    }
}
