package com.stock.pycurrent.entity.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author fzc
 * @date 2024/6/17 16:56
 * @description
 */

@Setter
@Getter
@NoArgsConstructor
public class RealTimeVO {
    private int di; //down_index
    private BigDecimal cp;//current_price
    private Long vs;//vol_step
    private BigDecimal h;//change_hand
    private BigDecimal rt;//pch_change
    private BigDecimal bar;
}
