package com.stock.pycurrent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "range_over_code")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RangeOverCode {
    @Id
    @Column(name = "trade_date", nullable = false, length = 8)
    private String tradeDate;

    @Column(name = "codes", length = 512)
    private String codes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangeOverCode that = (RangeOverCode) o;
        return Objects.equals(getTradeDate(), that.getTradeDate()) && Objects.equals(getCodes(), that.getCodes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTradeDate(), getCodes());
    }
}