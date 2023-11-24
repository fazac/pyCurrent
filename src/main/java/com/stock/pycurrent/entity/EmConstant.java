package com.stock.pycurrent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "em_constants")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmConstant {
    @Column(name = "c_key", length = 32)
    @Id
    private String cKey;

    @Column(name = "c_value", length = 256)
    private String cValue;
    
    @Column(name = "buy_price")
    private BigDecimal buyPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmConstant that = (EmConstant) o;
        return Objects.equals(cKey, that.cKey) && Objects.equals(cValue, that.cValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cKey, cValue);
    }
}