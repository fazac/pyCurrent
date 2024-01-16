package com.stock.pycurrent.entity.jsonvalue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author fzc
 * @date 2024/1/16 10:50
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LimitCodeValue implements Serializable {
    private String code;
    private int count;
    private boolean stayed;
    private boolean broken;
}
