package com.stock.pycurrent.entity.emum;

import lombok.Getter;

/**
 * @author fzc
 * @date 2024/6/19 13:55
 * @description
 */

@Getter
public enum SSEMsgEnum {
    RT_CURRENT("1"), RT_HIS("2"), RT_NOTIFICATION("3");
    private final String type;

    SSEMsgEnum(String type) {
        this.type = type;
    }

    /**
     * 根据类型的名称，返回类型的枚举实例。
     *
     * @param type 类型名称
     */
    public static SSEMsgEnum fromType(String type) {
        for (SSEMsgEnum sseMsgEnum : SSEMsgEnum.values()) {
            if (sseMsgEnum.getType().equals(type)) {
                return sseMsgEnum;
            }
        }
        return null;
    }

}
