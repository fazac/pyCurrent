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
    private BigDecimal cm;
    private BigDecimal currentPri;
    private ObjectNode extraNode;

}
