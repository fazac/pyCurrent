package com.stock.pycurrent.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author fzc
 * @date 2023/12/5 9:24
 * @description
 */
@Data
public class EmConstantValue implements Serializable {
    private String tsCode;
    private BigDecimal price;
    private Long vol;
    private boolean sellable;
}
