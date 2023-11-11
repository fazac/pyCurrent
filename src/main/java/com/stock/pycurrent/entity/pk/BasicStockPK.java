package com.stock.pycurrent.entity.pk;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BasicStockPK implements Serializable {
    String tsCode;
    String tradeDate;
}
