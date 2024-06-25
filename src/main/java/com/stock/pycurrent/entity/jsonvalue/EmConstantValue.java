package com.stock.pycurrent.entity.jsonvalue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fzc
 * @date 2023/12/5 9:24
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmConstantValue implements Serializable {
    private String tsCode;
    private BigDecimal price;
    private Long vol;
    private boolean sellable;
    private BigDecimal profit;
}
