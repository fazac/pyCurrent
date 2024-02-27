package com.stock.pycurrent.entity.pk;

import lombok.*;

import java.io.Serializable;

/**
 * @author fzc
 * @date 2023/10/30 14:33
 * @description
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ContinuousUpPK implements Serializable {
    String tsCode;
    String staticsDate;
}
