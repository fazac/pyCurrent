package com.stock.pycurrent.entity;

import com.stock.pycurrent.entity.jsonvalue.LimitCodeValue;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "limit_code")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LimitCode {
    @Id
    @Column(name = "trade_date", nullable = false, length = 8)
    private String tradeDate;

    @Column(columnDefinition = "jsonb", name = "code_value")
    @Type(JsonType.class)
    private List<LimitCodeValue> codeValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LimitCode limitCode = (LimitCode) o;
        return Objects.equals(getTradeDate(), limitCode.getTradeDate()) && Objects.equals(getCodeValue(), limitCode.getCodeValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTradeDate(), getCodeValue());
    }
}