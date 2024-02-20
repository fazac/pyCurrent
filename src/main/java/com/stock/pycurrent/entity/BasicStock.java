package com.stock.pycurrent.entity;

import com.stock.pycurrent.entity.pk.BasicStockPK;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.util.Objects;


@MappedSuperclass
@IdClass(BasicStockPK.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BasicStock {
    @Id
    @Column(name = "ts_code", length = 10)
    String tsCode;
    @Id
    @Column(name = "trade_date", length = 8)
    String tradeDate;

    @Column(name = "pri_open", precision = 18, scale = 3)
    BigDecimal priOpen;

    @Column(name = "pri_close", precision = 18, scale = 3)
    BigDecimal priClose;

    @Column(name = "pri_high", precision = 18, scale = 3)
    BigDecimal priHigh;

    @Column(name = "pri_low", precision = 18, scale = 3)
    BigDecimal priLow;

    @Column(name = "vol")
    Long vol;

    @Column(name = "amount", precision = 18, scale = 3)
    BigDecimal amount;

    @Column(name = "pct_chg", precision = 18, scale = 3)
    BigDecimal pctChg;

    @Column(name = "am_chg", precision = 18, scale = 3)
    BigDecimal amChg;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BasicStock that = (BasicStock) o;
        return tsCode != null && Objects.equals(tsCode, that.tsCode)
                && tradeDate != null && Objects.equals(tradeDate, that.tradeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tsCode, tradeDate);
    }
}
