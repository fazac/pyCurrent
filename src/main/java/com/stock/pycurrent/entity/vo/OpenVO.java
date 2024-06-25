package com.stock.pycurrent.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author fzc
 * @date 2024/6/25 14:24
 * @description
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OpenVO {
    private String ts_code;
    private String name;
    private BigDecimal pct_chg;
    private BigDecimal change_hand;
    private BigDecimal pe;
    private BigDecimal pb;
    private BigDecimal cap;
    private BigDecimal open;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal current_pri;
    private BigDecimal avg_pri;
    private BigDecimal pri_pre;
}
