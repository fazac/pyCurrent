package com.stock.pycurrent.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;
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

    @Column(columnDefinition = "jsonb", name = "code_value")
    @Type(JsonType.class)
    private List<RangeOverCodeValue> codeValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangeOverCode that = (RangeOverCode) o;
        return Objects.equals(getTradeDate(), that.getTradeDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTradeDate());
    }
}