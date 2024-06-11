package com.stock.pycurrent.entity.pk;

import jakarta.persistence.Column;
import lombok.*;

import java.io.Serializable;

/**
 * @author fzc
 * @date 2024/6/11 9:09
 * @description
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CurConcernCodePK implements Serializable {
    private String tradeDate;

    private String tsCode;
}
