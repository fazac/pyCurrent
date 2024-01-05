package com.stock.pycurrent.entity;

import com.stock.pycurrent.entity.pk.BasicStockPK;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Entity
@IdClass(BasicStockPK.class)
@Table(name = "real_bar")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RealBar {
    @Id
    @Column(name = "trade_date")
    private String tradeDate;

    @Id
    @Column(name = "ts_code")
    private String tsCode;

    @Column(name = "cur_pri")
    private BigDecimal curPri;

    @Column(name = "short_sma_price")
    private BigDecimal shortSmaPrice;

    @Column(name = "long_sma_price")
    private BigDecimal longSmaPrice;

    @Column(name = "dif")
    private BigDecimal dif;

    @Column(name = "dea")
    private BigDecimal dea;

    @Column(name = "bar")
    private BigDecimal bar;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealBar realBar = (RealBar) o;
        return Objects.equals(getTradeDate(), realBar.getTradeDate()) && Objects.equals(getTsCode(), realBar.getTsCode()) && Objects.equals(getCurPri(), realBar.getCurPri()) && Objects.equals(getShortSmaPrice(), realBar.getShortSmaPrice()) && Objects.equals(getLongSmaPrice(), realBar.getLongSmaPrice()) && Objects.equals(getDif(), realBar.getDif()) && Objects.equals(getDea(), realBar.getDea()) && Objects.equals(getBar(), realBar.getBar());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTradeDate(), getTsCode(), getCurPri(), getShortSmaPrice(), getLongSmaPrice(), getDif(), getDea(), getBar());
    }
}