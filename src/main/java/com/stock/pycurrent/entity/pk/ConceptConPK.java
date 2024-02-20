package com.stock.pycurrent.entity.pk;

import lombok.*;

import java.io.Serializable;

/**
 * @author fzc
 * @date 2023/7/3 17:31
 * @description
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ConceptConPK implements Serializable {
    String tsCode;
    String tradeDate;
    String symbol;
}
