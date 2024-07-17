package com.stock.pycurrent.entity.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author fzc
 * @date 2024/6/19 15:48
 * @description
 */

@Setter
@Getter
@NoArgsConstructor
public class DnVO {
    private int di;
    private String tradeDate;
    private BigDecimal cp;
    private BigDecimal hp;
    private BigDecimal lp;
    private BigDecimal ap;
    private BigDecimal h;
    private Long vol;
}
