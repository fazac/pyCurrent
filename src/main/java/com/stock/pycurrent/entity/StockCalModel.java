package com.stock.pycurrent.entity;

import com.stock.pycurrent.entity.pk.BasicStockPK;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "stock_cal_model")
@IdClass(BasicStockPK.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockCalModel {
    @Id
    @Column(name = "ts_code")
    private String tsCode;
    @Id
    @Column(name = "trade_date")
    private String tradeDate;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "level")
    private int level;
    @Column(name = "type")
    private int type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockCalModel that = (StockCalModel) o;
        return level == that.level && type == that.type && Objects.equals(tsCode, that.tsCode) && Objects.equals(tradeDate, that.tradeDate) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tsCode, tradeDate, price, level, type);
    }
}
