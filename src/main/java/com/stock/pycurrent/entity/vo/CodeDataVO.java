package com.stock.pycurrent.entity.vo;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author fzc
 * @date 2024/7/8 9:14
 * @description
 */
@Setter
@Getter
@NoArgsConstructor
public class CodeDataVO {
    private String code;
    private String labels;
    private BigDecimal pe;
    private BigDecimal pb;
    private BigDecimal cap;
    private BigDecimal currentPri;
    private BigDecimal lastFivePri;
    private BigDecimal lastTenPri;
    private BigDecimal lastTwentyPri;
    private BigDecimal lastThirtyPri;
    private BigDecimal lastFiftyPri;

    private ObjectNode objectNode;

}
