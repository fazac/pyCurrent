package com.stock.pycurrent.entity.jsonvalue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author fzc
 * @date 2023/12/29 14:39
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RangeOverCodeValue implements Serializable {
    private String code;
    private int count;
}
