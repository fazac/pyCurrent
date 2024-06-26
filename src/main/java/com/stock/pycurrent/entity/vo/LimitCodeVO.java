package com.stock.pycurrent.entity.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author fzc
 * @date 2024/6/26 16:22
 * @description
 */
@Setter
@Getter
@NoArgsConstructor
public class LimitCodeVO {
    private String code;
    private int count;
    private String labels;
    private BigDecimal pe;
    private BigDecimal pb;
    private BigDecimal cap;

    private BigDecimal hand;
    private BigDecimal hlc;
    private BigDecimal ac;
    private BigDecimal cc;
}